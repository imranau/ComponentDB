/*
 * Copyright (c) UChicago Argonne, LLC. All rights reserved.
 * See LICENSE file.
 */
package gov.anl.aps.cdb.portal.model.db.entities;

import gov.anl.aps.cdb.portal.constants.ItemDomainName;
import gov.anl.aps.cdb.portal.constants.ItemElementRelationshipTypeNames;
import gov.anl.aps.cdb.portal.controllers.RelationshipTypeController;
import gov.anl.aps.cdb.portal.model.db.beans.RelationshipTypeFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author cmcchesney
 */
@Entity
@DiscriminatorValue(value = ItemDomainName.CABLE_DESIGN_ID + "")
public class ItemDomainCableDesign extends Item {

    private static final String endpointsSeparator = " | ";

    @Override
    public Item createInstance() {
        return new ItemDomainCableDesign();
    }

    public List<Item> getEndpointList() {
        {
            ItemElement selfElement = this.getSelfElement();
            List<ItemElementRelationship> ierList
                    = selfElement.getItemElementRelationshipList1();
            if (ierList != null) {
                // find just the cable relationship items
                RelationshipType cableIerType
                        = RelationshipTypeFacade.getInstance().findByName(
                                ItemElementRelationshipTypeNames.itemCableConnection.getValue());
                if (cableIerType != null) {
                    return ierList.stream().
                            filter(ier -> ier.getRelationshipType().getName().equals(cableIerType.getName())).
                            map(ier -> ier.getFirstItemElement().getParentItem()).
                            collect(Collectors.toList());
                }
            }

            return null;
        }
    }
    
    private RelationshipType getCableConnectionRelationshipType() {
        RelationshipType relationshipType = 
                RelationshipTypeFacade.getInstance().findByName(
                        ItemElementRelationshipTypeNames.itemCableConnection.getValue());
        if (relationshipType == null) {
            RelationshipTypeController controller = RelationshipTypeController.getInstance();
            String name = ItemElementRelationshipTypeNames.itemCableConnection.getValue();
            relationshipType = controller.createRelationshipTypeWithName(name);
        }
        return relationshipType;
    }    

    /**
     * Creates ItemElementRelationship for the 2 specified items.
     * @param item Machine design item for cable endpoint.
     * @return New instance of ItemElementRelationshipo for specified items.
     */
    private ItemElementRelationship createRelationship(Item item) {
        
        ItemElementRelationship itemElementRelationship = new ItemElementRelationship();
        itemElementRelationship.setFirstItemElement(item.getSelfElement());
        itemElementRelationship.setSecondItemElement(this.getSelfElement());

        RelationshipType cableConnectionRelationshipType = getCableConnectionRelationshipType();
        itemElementRelationship.setRelationshipType(cableConnectionRelationshipType);

        return itemElementRelationship;
    }

    /**
     * Adds specified relationship for specified item.
     * @param item Item to add relationship for.
     * @param ier Relationship to add.
     * @param secondItem True if the item is the second item in the relationship.
     */
    private void addItemElementRelationshipToItem(Item item, ItemElementRelationship ier, boolean secondItem) {
        ItemElement selfElement = item.getSelfElement();
        List<ItemElementRelationship> ierList;
        if (secondItem) {
            ierList = selfElement.getItemElementRelationshipList1();
        } else {
            ierList = selfElement.getItemElementRelationshipList();
        }
        ierList.add(ier);
    }
    
    private void addCableRelationship(Item endpoint) {       
        // create relationships from cable to endpoints
        ItemElementRelationship relationship = createRelationship(endpoint);

        // Create list for cable's relationships. 
        ItemElement selfElement = this.getSelfElement();
        if (selfElement.getItemElementRelationshipList1() == null) {
            selfElement.setItemElementRelationshipList1(new ArrayList<>());
        }

        // Add appropriate item relationships to model.
        addItemElementRelationshipToItem(endpoint, relationship, false);
        addItemElementRelationshipToItem(this, relationship, true);
    }
    
    public void setEndpoint1(Item itemEndpoint1) {
        this.addCableRelationship(itemEndpoint1);
    }
    
    public void setEndpoint2(Item itemEndpoint2) {
        this.addCableRelationship(itemEndpoint2);
    }
    
    /**
     * Updates oldEndpoint to newEndpoint.
     * @param oldEndpoint
     * @param newEndpoint 
     */
    public Boolean updateEndpoint(Item oldEndpoint, Item newEndpoint) {

        ItemElement selfElement = this.getSelfElement();
        List<ItemElementRelationship> ierList = selfElement.getItemElementRelationshipList1();
        
        if (ierList != null) {

            RelationshipType cableIerType
                    = RelationshipTypeFacade.getInstance().findByName(
                            ItemElementRelationshipTypeNames.itemCableConnection.getValue());

            // find cable relationship for old endpoint
            ItemElementRelationship cableRelationship = ierList.stream()
                    .filter(ier -> (ier.getRelationshipType().getName().equals(cableIerType.getName()))
                    && (ier.getFirstItemElement().equals(oldEndpoint.getSelfElement())))
                    .findAny()
                    .orElse(null);
            
            // update cable relationship to new endpoint
            if (cableRelationship != null) {
                cableRelationship.setFirstItemElement(newEndpoint.getSelfElement());
                // null out connector too, for when we add support for port-level connections
                cableRelationship.setFirstItemConnector(null);
            }
        }
        
        return true;
    }
    
    /**
     * Returns a string containing the cables endpoints for display.
     */
    public String getEndpointsString() {
        String result = "";
        int count = 0;
        List<Item> iList = this.getEndpointList();      
        for (Item endpoint : iList) {
            count = count + 1;
            result = result + endpoint.getName();
            if (count != iList.size()) {
                result = result + endpointsSeparator;
            }
        }
        return result;
    }

    public Item getEndpoint1() {
        List<Item> iList = this.getEndpointList();
        if (iList.size() > 0) {
            return iList.get(0);
        }
        else {
            return null;
        }
    }

    public String getEndpoint1String() {
        Item iEndpoint1 = this.getEndpoint1();
        if (iEndpoint1 != null) {
            return iEndpoint1.getName();
        } else {
            return "";
        }
    }

    public Item getEndpoint2() {
        List<Item> iList = this.getEndpointList();
        if (iList.size() > 1) {
            return iList.get(1);
        }
        else {
            return null;
        }
    }

    public String getEndpoint2String() {
        Item iEndpoint2 = this.getEndpoint2();
        if (iEndpoint2 != null) {
            return iEndpoint2.getName();
        } else {
            return "";
        }
    }
    
    public void setCatalogItem(Item itemCableCatalog) {
        // "assign" catalog item to cable design
        ItemElement selfElement = this.getSelfElement();
        selfElement.setContainedItem2(itemCableCatalog);
    }
    
    public Item getCatalogItem() {
        ItemElement selfElementCable = this.getSelfElement();
        return selfElementCable.getContainedItem2();       
    }

    public String getCatalogItemString() {
        Item iCatalog = this.getCatalogItem();
        if (iCatalog != null) {
            return iCatalog.getName();
        } else {
            return "";
        }
    }

}
