/*
 * Copyright (c) UChicago Argonne, LLC. All rights reserved.
 * See LICENSE file.
 */
package gov.anl.aps.cdb.portal.controllers;

import gov.anl.aps.cdb.common.exceptions.CdbException;
import gov.anl.aps.cdb.common.exceptions.ObjectAlreadyExists;
import gov.anl.aps.cdb.common.utilities.ObjectUtility;

import gov.anl.aps.cdb.portal.controllers.settings.ItemElementSettings;
import gov.anl.aps.cdb.portal.model.db.entities.ItemElement;
import gov.anl.aps.cdb.portal.model.db.beans.ItemElementFacade;
import gov.anl.aps.cdb.portal.model.db.entities.EntityInfo;
import gov.anl.aps.cdb.portal.model.db.entities.Item;
import gov.anl.aps.cdb.portal.model.db.entities.ItemDomainCatalog;
import gov.anl.aps.cdb.portal.model.db.entities.PropertyValue;
import gov.anl.aps.cdb.portal.model.db.utilities.EntityInfoUtility;
import gov.anl.aps.cdb.portal.model.db.utilities.ItemElementUtility;
import gov.anl.aps.cdb.portal.model.db.utilities.PropertyValueUtility;
import gov.anl.aps.cdb.portal.utilities.SessionUtility;
import gov.anl.aps.cdb.portal.view.objects.CatalogItemElementConstraintInformation;
import gov.anl.aps.cdb.portal.view.objects.ItemElementConstraintInformation;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.ReorderEvent;
import org.primefaces.model.TreeNode;

@Named("itemElementController")
@SessionScoped
public class ItemElementController extends CdbDomainEntityController<ItemElement, ItemElementFacade, ItemElementSettings> implements Serializable {

    @EJB
    private ItemElementFacade itemElementFacade;

    private static final Logger logger = Logger.getLogger(ItemElementController.class.getName());

    private static final String DESIGN_ELEMENT_ROW_COLOR_PROPERTY_NAME = "Item Element Row Color";

    private Item selectedParentItem = null;

    private List<PropertyValue> filteredPropertyValueList = null;

    private List<ItemElement> pendingChangesItemElementList = null;
    private List<ItemElement> sortableItemElementList = null;

    private ItemController currentSettingsItemController = null;

    private ItemController currentItemReorderController = null;
    
    private static ItemElementController apiInstance; 

    public ItemElementController() {
    }
    
    public static synchronized ItemElementController getApiInstance() {
        if (apiInstance == null) {
            apiInstance = new ItemElementController();            
            apiInstance.prepareApiInstance(); 
        }
        return apiInstance;
    } 

    @Override
    protected void loadEJBResourcesManually() {
        super.loadEJBResourcesManually(); 
        
        itemElementFacade = ItemElementFacade.getInstance(); 
    }

    public static ItemElementController getInstance() {
        if (SessionUtility.runningFaces()) {
            return (ItemElementController) SessionUtility.findBean("itemElementController");
        } else {
            return getApiInstance(); 
        }
        
    }

    public void resetCurrentItemVariables() {
        currentSettingsItemController = null;
        currentItemReorderController = null;
    }

    public ItemController getCurrentSettingsItemController() {
        return currentSettingsItemController;
    }

    public void setCurrentSettingsItemController(ItemController itemController) {
        currentSettingsItemController = itemController;
    }

    @Override
    protected void prepareEntityUpdate(ItemElement itemElement) throws CdbException {
        super.prepareEntityUpdate(itemElement);
        
        EntityInfo entityInfo = itemElement.getEntityInfo();
        
        if (apiMode) {
            EntityInfoUtility.updateEntityInfo(entityInfo, apiUser);
        } else {
            EntityInfoUtility.updateEntityInfo(entityInfo);
        }
        
        ItemElement originalItemElement = null; 
        if (itemElement.getId() != null) {
            originalItemElement = getEntityDbFacade().find(itemElement.getId());
        }
        
        // Check if history needs to be added
        ItemElementUtility.prepareItemElementHistory(originalItemElement, itemElement, entityInfo);        

        // Basic checks for updating an element must be verified with domain of item element. 
        Item parentItem = itemElement.getParentItem();
        ItemController itemController = ItemController.findDomainControllerForItem(parentItem);
        itemController.checkItemElement(itemElement);

        if (itemElement.getId() != null) {
            ItemElement freshDbItemElement = findById(itemElement.getId());

            // Verify if contained item changed
            Item originalContainedItem = freshDbItemElement.getContainedItem();
            if (ObjectUtility.equals(originalContainedItem, itemElement.getContainedItem()) == false) {
                // Contained item has been updated.
                ItemElementConstraintInformation ieci = getItemElementConstraintInformation(freshDbItemElement);
                if (ieci.isSafeToUpdateContainedItem() == false) {
                    itemElement.setContainedItem(originalContainedItem);
                    throw new CdbException("Cannot update item element " + itemElement + " due to constraints not met. Please reload the item details page and try again.");
                }
            }

            //Verify if isRequred changed
            Boolean originalIsRequired = freshDbItemElement.getIsRequired();
            if (ObjectUtility.equals(originalIsRequired, itemElement.getIsRequired()) == false) {
                itemController.finalizeItemElementRequiredStatusChanged(itemElement);
            }
        }
    }

    @Override
    protected void completeEntityUpdate(ItemElement itemElement) {
        // Force update of constraint information. 
        itemElement.setConstraintInformation(null);

        Item parentItem = itemElement.getParentItem();
        ItemController itemController = ItemController.findDomainControllerForItem(parentItem);
        itemController.completeSuccessfulItemElementUpdate(itemElement);
    }

    @Override
    protected void prepareEntityDestroy(ItemElement itemElement) throws CdbException {
        super.prepareEntityDestroy(itemElement);
        // Verify that item domain allows destroy of item element.   

        Item parentItem = itemElement.getParentItem();
        if (!(parentItem instanceof ItemDomainCatalog)) {
            ItemElementConstraintInformation ieci = getFreshItemElementConstraintInformation(itemElement);
            if (ieci.isSafeToRemove() == false) {
                String constraintMessage = generateSingleDeleteConstraintMessage(itemElement);
                throw new CdbException("Cannot remove item element. Constrains not met. " + constraintMessage);
            }
        }
    }

    private String generateSingleDeleteConstraintMessage(ItemElement itemElement) {
        ItemElementConstraintInformation constraint = getItemElementConstraintInformation(itemElement);
        String message = null;

        if (constraint instanceof CatalogItemElementConstraintInformation) {
            CatalogItemElementConstraintInformation catalogConstraint = (CatalogItemElementConstraintInformation) constraint;
            message = generateCatalogSpecificPreventDeleteUpdateContainedMessage(catalogConstraint);
        }
        if (message == null) {
            // Check set log and properties 
            if (constraint.isHasLogs()) {
                return "Item element has logs.";
            } else if (constraint.isHasProperties()) {
                return "Item element has properties";
            }

            List<ItemElementConstraintInformation> relatedConstraintInfo = constraint.getRelatedConstraintInfo();
            for (ItemElementConstraintInformation ittrConstraint : relatedConstraintInfo) {
                String itemElementIdentifyingString = "Related item element of: " + ittrConstraint.getItemElement().getParentItem().getName();
                if (ittrConstraint.isHasLogs()) {
                    return itemElementIdentifyingString + " has logs.";
                } else if (constraint.isHasProperties()) {
                    return itemElementIdentifyingString + " has properties.";
                }
            }

        } else {
            return message;
        }

        return "";
    }

    @Override
    protected void completeEntityDestroy(ItemElement itemElement) {
        // Verify that item domain allows destroy of item element. 
        Item parentItem = itemElement.getParentItem();
        ItemController itemController = ItemController.findDomainControllerForItem(parentItem);
        itemController.completeSuccessfulItemElementRemoval(itemElement);
    }

    private String generateCatalogSpecificPreventDeleteUpdateContainedMessage(CatalogItemElementConstraintInformation catalogConstraint) {
        List<ItemElementConstraintInformation> relatedConstraintInfo = catalogConstraint.getRelatedConstraintInfo();

        for (ItemElementConstraintInformation ittrConstraint : relatedConstraintInfo) {
            if (ittrConstraint instanceof CatalogItemElementConstraintInformation) {
                if (((CatalogItemElementConstraintInformation) ittrConstraint).isHasInventoryItemAssigned()) {
                    String message = ittrConstraint.getItemElement().getParentItem().getName();
                    message += " has an inventory item assinged for this element.";
                    return message;
                }
            }
        }
        return null;
    }

    public void openEditContainedItemForCurrent(String onSuccessCommand) {
        ItemElementConstraintInformation constraint = getItemElementConstraintInformation(getCurrent());

        if (constraint.isSafeToUpdateContainedItem()) {            
            SessionUtility.executeRemoteCommand(onSuccessCommand);
        } else {
            String errorMessage = "";

            if (constraint instanceof CatalogItemElementConstraintInformation) {
                CatalogItemElementConstraintInformation catalogConstraint = (CatalogItemElementConstraintInformation) constraint;
                errorMessage = generateCatalogSpecificPreventDeleteUpdateContainedMessage(catalogConstraint);
                if (errorMessage == null) {
                    errorMessage = "";
                }
            }

            SessionUtility.addErrorMessage("Cannot Edit Contained Item", errorMessage);
        }
    }

    public ItemElementConstraintInformation getItemElementConstraintInformation(ItemElement itemElement) {
        ItemElementConstraintInformation itemElementConstraintInformation = null;
        if (itemElement != null) {
            itemElementConstraintInformation = itemElement.getConstraintInformation();
            if (itemElementConstraintInformation == null) {
                Item parentItem = itemElement.getParentItem();
                if (parentItem != null) {
                    ItemController itemDomainController = ItemController.findDomainControllerForItem(parentItem);
                    itemElementConstraintInformation = itemDomainController.loadItemElementConstraintInformation(itemElement);
                    itemElement.setConstraintInformation(itemElementConstraintInformation);
                }
            }
        }
        return itemElementConstraintInformation;
    }

    public ItemElementConstraintInformation getFreshItemElementConstraintInformation(ItemElement itemElement) {
        itemElement = findById(itemElement.getId());
        return getItemElementConstraintInformation(itemElement);
    }

    public void setValidContainedItemForCurrent(Item containedItem) {
        if (current.getId() == null) {
            current.setContainedItem(containedItem);
            return;
        } else {
            ItemElementConstraintInformation ieci = getFreshItemElementConstraintInformation(current);
            if (ieci.isSafeToUpdateContainedItem()) {
                current.setContainedItem(containedItem);
                return;
            }
        }

        SessionUtility.addErrorMessage("Constraints not met", "Couldn't update contained item due to certain constraints. Refresh the detail page and try again.");
    }

    public void revertContainedItemForCurrent() {
        if (current != null) {
            ItemElement itemElement = findById(current.getId());
            Item containedItem = itemElement.getContainedItem();

            current.setContainedItem(containedItem);
        }
    }

    public void toggleIsRequiredForCurrent() {
        if (current != null) {
            Boolean isRequired = current.getTemporaryIsRequiredValue();
            current.setTemporaryIsRequiredValue(!isRequired);
        }
    }

    public String getIsRequiredButtonValueForCurrent() {
        if (current != null) {
            Boolean isRequired = current.getTemporaryIsRequiredValue();
            if (isRequired) {
                return "Yes";
            } else {
                return "No";
            }
        }
        return null;
    }

    public void revertIsRequiredItemForCurrent() {
        if (current != null) {
            current.setTemporaryIsRequiredValue(null);
        }
    }

    public void submitIsRequiredValueForCurrent() {
        current.setIsRequired(current.getTemporaryIsRequiredValue());
        updateWithoutRedirect();
    }

    @Override
    protected ItemElementFacade getEntityDbFacade() {
        return itemElementFacade;
    }

    @Override
    protected ItemElement createEntityInstance() {
        ItemElement designElement = new ItemElement();
        EntityInfo entityInfo = EntityInfoUtility.createEntityInfo();
        designElement.setEntityInfo(entityInfo);

        // clear selection lists
        return designElement;
    }

    @Override
    public ItemElement findById(Integer id) {
        return itemElementFacade.findById(id);
    }

    @Override
    public EntityInfo getEntityInfo(ItemElement entity) {
        if (entity != null) {
            return entity.getEntityInfo();
        }
        return null;
    }

    public boolean isDisplayRowExpansionForItemElement(ItemElement itemElement) {
        if (settingObject.getDisplayFlatTableView() && settingObject.getDisplayRowExpansion()) {
            return isDisplayRowExpansionElementsList(itemElement)
                    || isDisplayRowExpansionLogs(itemElement)
                    || isDisplayRowExpansionProperties(itemElement);
        }
        return false;
    }

    public boolean isDisplayRowExpansionElementsList(ItemElement itemElement) {
        if (itemElement.getContainedItem() != null) {
            return !itemElement.getContainedItem().getItemElementDisplayList().isEmpty();
        }
        return false;
    }

    public TreeNode getItemElementListTreeTableRootNode(ItemElement parent) {

        if (parent.getChildItemElementListTreeTableRootNode() == null) {
            try {
                if (parent.getContainedItem() != null) {
                    parent.setChildItemElementListTreeTableRootNode(ItemElementUtility.createItemElementRoot(parent.getContainedItem()));
                }
            } catch (CdbException ex) {
                logger.debug(ex);
            }
        }

        return parent.getChildItemElementListTreeTableRootNode();
    }

    @Override
    public String getEntityTypeName() {
        return "itemElement";
    }

    @Override
    public String getDisplayEntityTypeName() {
        return "Item Element";
    }

    @Override
    public String getCurrentEntityInstanceName() {
        if (getCurrent() != null) {
            return getCurrent().getName();
        }
        return "";
    }

    @Override
    public List<ItemElement> getAvailableItems() {
        return super.getAvailableItems();
    }

    @Override
    public void prepareEntityInsert(ItemElement designElement) throws ObjectAlreadyExists {
    }

    /*
    @Override
    public void prepareEntityUpdate(ItemElement designElement) throws ObjectAlreadyExists {
        EntityInfo entityInfo = designElement.getEntityInfo();
        EntityInfoUtility.updateEntityInfo(entityInfo);

        // Compare properties with what is in the db
        List<PropertyValue> originalPropertyValueList = designElementFacade.findById(designElement.getId()).getPropertyValueList();
        List<PropertyValue> newPropertyValueList = designElement.getPropertyValueList();
        logger.debug("Verifying properties for design element " + designElement);
        PropertyValueUtility.preparePropertyValueHistory(originalPropertyValueList, newPropertyValueList, entityInfo);
        prepareImageList(designElement);
        resetSelectObjectLists();
    }
     */
    // This listener is accessed either after selection made in dialog,
    // or from selection menu.    
    public void selectItemValueChangeListener(ValueChangeEvent valueChangeEvent) {
        ItemElement itemElement = getCurrent();
        if (itemElement == null || valueChangeEvent == null) {
            return;
        }

        Item existingItem = itemElement.getContainedItem();
        Item newEventItem = null;
        Item oldEventItem = null;

        Object newValue = valueChangeEvent.getNewValue();
        if (newValue != null) {
            newEventItem = (Item) newValue;
        }
        Object oldValue = valueChangeEvent.getOldValue();
        if (oldValue != null) {
            oldEventItem = (Item) oldValue;
        }

        if (ObjectUtility.equals(existingItem, oldEventItem)) {
            // change via menu
            itemElement.setContainedItem(newEventItem);
        } else {
            // change via dialog
            itemElement.setContainedItem(oldEventItem);
        }
    }

    @Override
    public void prepareEntityUpdateOnRemoval(ItemElement designElement) {
        EntityInfo entityInfo = designElement.getEntityInfo();
        EntityInfoUtility.updateEntityInfo(entityInfo);
        prepareImageList(designElement);
    }

    @Override
    public String prepareView(ItemElement itemElement) {
        logger.debug("Preparing item element view");
        super.prepareView(itemElement);
        return "/views/itemElement/view.xhtml?faces-redirect=true";
    }

    public void prepareAddProperty() {
        ItemElement designElement = getCurrent();
        List<PropertyValue> propertyList = designElement.getPropertyValueList();
        PropertyValue property = new PropertyValue();
        propertyList.add(property);
    }

    public String destroyAndReturnItemView(ItemElement designElement) {
        Item parentItem = designElement.getParentItem();
        setCurrent(designElement);
        try {
            logger.debug("Destroying " + designElement.getName());
            getEntityDbFacade().remove(designElement);
            SessionUtility.addInfoMessage("Success", "Deleted design element id " + designElement.getId() + ".");
            return "/views/design/view.xhtml?faces-redirect=true&id=" + parentItem.getId();
        } catch (Exception ex) {
            SessionUtility.addErrorMessage("Error", "Could not delete " + getDisplayEntityTypeName() + ": " + ex.getMessage());
            return null;
        }
    }

    @Override
    public void resetDomainEntityPropertyTypeIdIndexMappings() {
        super.resetDomainEntityPropertyTypeIdIndexMappings();
        ItemElement.setSortByPropertyTypeId(settingObject.getSortByPropertyTypeId());
    }

    public String getDisplayRowStyle(ItemElement itemElement) {
        List<String> rowStyles = new ArrayList<>();
        Boolean displayItemElementRowColor = settingObject.getDisplayItemElementRowColor();
        if (displayItemElementRowColor != null && displayItemElementRowColor) {
            List<PropertyValue> propertyValueList = itemElement.getPropertyValueList();
            if (propertyValueList != null && propertyValueList.size() > 0) {
                for (PropertyValue propertyValue : propertyValueList) {
                    if (propertyValue.getPropertyType().getName().equals(DESIGN_ELEMENT_ROW_COLOR_PROPERTY_NAME)) {
                        String value = propertyValue.getValue();
                        rowStyles.add(value + "Row");
                        break;
                    }
                }
            }
        }

        Boolean isRequired = itemElement.getIsRequired();
        if (isRequired != null && isRequired == false) {
            rowStyles.add("optionalElement");
        }

        String style = "";
        for (String currentStyle : rowStyles) {
            style += currentStyle + " ";
        }

        return style;
    }

    public Item getSelectedParentItem() {
        return selectedParentItem;
    }

    public void onListDataTableRowReorder(ReorderEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        List<ItemElement> itemElementList = (List<ItemElement>) dataTable.getValue();

        ItemElement ie = itemElementList.get(0);
        Item parentItem = ie.getParentItem();
        currentItemReorderController = ItemController.findDomainControllerForItem(parentItem);
        if (!currentItemReorderController.getCurrent().equals(parentItem)) {
            // controller is not displaying parent item. 
            return;
        }

        // Verify list belongs to same item
        int origSize = parentItem.getItemElementDisplayList().size();
        if (itemElementList.size() == origSize) {
            for (ItemElement itemElement : itemElementList) {
                if (!itemElement.getParentItem().equals(parentItem)) {
                    // Invalid list in the data table. 
                    return;
                }
            }
        }

        // Update the sort order of the list items
        for (int i = 0; i < itemElementList.size(); i++) {
            ItemElement itemElement = itemElementList.get(i);
            float orderedNumber = i;
            itemElement.setSortOrder(orderedNumber);
        }

        currentItemReorderController.setHasElementReorderChangesForCurrent(true);
    }

    public void performReorderSaveOperations() {
        if (currentItemReorderController != null) {
            currentItemReorderController.update();
        }
    }

    public boolean getHasReorderChanges() {
        if (currentItemReorderController != null) {
            return currentItemReorderController.getHasElementReorderChangesForCurrent();
        }
        return false;
    }

    /**
     * Function handles the sorting of dropped designElement based on where it
     * was dropped.
     *
     * @param ddEvent
     */
    public void onItemElementDrop(DragDropEvent ddEvent) {
        String[] draggedId = ddEvent.getDragId().split(":");
        String[] droppedId = ddEvent.getDropId().split(":");
        int dragIndex = Integer.parseInt(draggedId[draggedId.length - 2]);
        int dropIndex = Integer.parseInt(droppedId[droppedId.length - 2]);

        String dropDescription = droppedId[droppedId.length - 1];

        if (dragIndex > dropIndex) { //Dragged from bottom to top
            if (dropDescription.equals("designElementRearrangeDataGridBottomPanel")) {
                dropIndex++;
            }
        } else if (dragIndex < dropIndex) { //Dragged from top to bottom
            if (dropDescription.equals("designElementRearrangeDataGridTopPanel")) {
                dropIndex--;
            }
        }

        if (dragIndex == dropIndex) {
            return;
        }
        ItemElement designElementDroppped = (ItemElement) ddEvent.getData();

        List<ItemElement> designElementList = sortableItemElementList;

        List<ItemElement> newItemElementList = new ArrayList<>();

        float sortOrder = 1;
        for (int i = 0; i < designElementList.size(); i++) {
            if (dragIndex > dropIndex) {
                if (dragIndex == i) {
                    continue;
                }
                if (dropIndex == i) {
                    newItemElementList.add(designElementDroppped);
                    updateItemElementSortOrder(designElementDroppped, sortOrder++);
                }
                newItemElementList.add(designElementList.get(i));
                updateItemElementSortOrder(designElementList.get(i), sortOrder++);
            } else if (dragIndex < dropIndex) {
                if (dragIndex == i) {
                    continue;
                }
                newItemElementList.add(designElementList.get(i));
                updateItemElementSortOrder(designElementList.get(i), sortOrder++);
                if (dropIndex == i) {
                    newItemElementList.add(designElementDroppped);
                    updateItemElementSortOrder(designElementDroppped, sortOrder++);
                }
            }
        }

        sortableItemElementList = newItemElementList;
    }

    public List<ItemElement> getSortableItemElementList() {
        return sortableItemElementList;
    }

    /**
     * Creates a sorted list in preparation for sorting.
     *
     * @param parentItem
     * @param onSuccessCommand
     */
    public void configureSortableItemElementList(Item parentItem, String onSuccessCommand) {
        if (pendingChangesItemElementList != null) {
            pendingChangesItemElementList.clear();
        }

        List<ItemElement> parentItemElementList = parentItem.getItemElementDisplayList();

        if (parentItemElementList != null && parentItemElementList.size() > 0) {
            sortableItemElementList = new ArrayList<>();

            for (ItemElement designElement : parentItemElementList) {
                if (designElement.getId() == null) {
                    SessionUtility.addWarningMessage("Unsaved Item Element", "Please save design element list and try again.");
                    return;
                }
                sortableItemElementList.add(designElement);
            }
            // Apply a sort in case the user applied filters to the data table
            Collections.sort(sortableItemElementList, new Comparator<ItemElement>() {
                @Override
                public int compare(ItemElement e1, ItemElement e2) {
                    if (e1.getSortOrder() != null && e2.getSortOrder() != null) {
                        return e1.getSortOrder().compareTo(e2.getSortOrder());
                    }
                    return 1;
                }
            });
            
            SessionUtility.executeRemoteCommand(onSuccessCommand);
        } else {
            SessionUtility.addInfoMessage("Info", "No design elements to sort.");
        }

    }

    /**
     * Updates the design element with new sort order number if needed adds to
     * list that will be saved once sorting is complete.
     *
     * @param designElement object to have the sort order updated
     * @param sortOrder sort order to set on the designElement object
     */
    private void updateItemElementSortOrder(ItemElement designElement, float sortOrder) {
        if (designElement.getSortOrder() != null && designElement.getSortOrder() == sortOrder) {
            return;
        }
        designElement.setSortOrder(sortOrder);

        if (pendingChangesItemElementList == null) {
            pendingChangesItemElementList = new ArrayList<>();
        }
        if (pendingChangesItemElementList.contains(designElement) == false) {
            pendingChangesItemElementList.add(designElement);
        }
    }

    @Override
    public Boolean getDisplayUpdateSortOrderButton() {
        return true;
    }

    /**
     * Determines if pending changes need to be saved by checking the list.
     *
     * @return boolean
     */
    public Boolean getItemElementPendingChanges() {
        return pendingChangesItemElementList != null && pendingChangesItemElementList.size() > 0;
    }

    /**
     * Updates each design element object that had the sort order modified.
     */
    public void saveItemElementPendingChanges() {
        if (getItemElementPendingChanges()) {
            for (int i = 0; i < pendingChangesItemElementList.size(); i++) {
                this.setCurrent(pendingChangesItemElementList.get(i));
                this.update();
            }

            // Need the server to refresh database information for the particular entity.
            int parentItemId = pendingChangesItemElementList.get(0).getParentItem().getId();
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("?id=" + parentItemId);
            } catch (IOException ex) {
                logger.debug(ex);
                SessionUtility.addErrorMessage("Error", ex.getMessage());
            }

            // Clear the pending changes list since everythign was saved
            pendingChangesItemElementList.clear();
            // Clear the sortable list pointer since sorting is complete. 
            sortableItemElementList = null;

        }
    }

    public void prepareComponentInstancePropertyValueDisplay(ItemElement designElement) {
        List<PropertyValue> propertyValueList = designElement.getPropertyValueList();
        for (PropertyValue propertyValue : propertyValueList) {
            PropertyValueUtility.configurePropertyValueDisplay(propertyValue);
        }
    }

    @Override
    protected ItemElementSettings createNewSettingObject() {
        return new ItemElementSettings(this);
    }

}
