/*
 * Copyright (c) UChicago Argonne, LLC. All rights reserved.
 * See LICENSE file.
 */
package gov.anl.aps.cdb.portal.controllers;

import gov.anl.aps.cdb.common.exceptions.ObjectAlreadyExists;
import gov.anl.aps.cdb.portal.controllers.settings.PropertyTypeHandlerSettings;
import gov.anl.aps.cdb.portal.model.db.beans.PropertyTypeHandlerFacade;
import gov.anl.aps.cdb.portal.model.db.entities.PropertyTypeHandler;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.log4j.Logger;

@Named("propertyTypeHandlerController")
@SessionScoped
public class PropertyTypeHandlerController extends CdbEntityController<PropertyTypeHandler, PropertyTypeHandlerFacade, PropertyTypeHandlerSettings> implements Serializable {

    private static final Logger logger = Logger.getLogger(PropertyTypeHandlerController.class.getName());

    @EJB
    private PropertyTypeHandlerFacade propertyTypeHandlerFacade;

    public PropertyTypeHandlerController() {
    }

    @Override
    protected PropertyTypeHandlerFacade getEntityDbFacade() {
        return propertyTypeHandlerFacade;
    }

    @Override
    protected PropertyTypeHandler createEntityInstance() {
        PropertyTypeHandler propertyHandler = new PropertyTypeHandler();
        return propertyHandler;
    }

    @Override
    public String getEntityTypeName() {
        return "propertyTypeHandler";
    }

    @Override
    public String getDisplayEntityTypeName() {
        return "property type handler";
    }

    @Override
    public String getCurrentEntityInstanceName() {
        if (getCurrent() != null) {
            return getCurrent().getName();
        }
        return "";
    }

    @Override
    public List<PropertyTypeHandler> getAvailableItems() {
        return super.getAvailableItems();
    }

    @Override
    public void prepareEntityInsert(PropertyTypeHandler propertyTypeHandler) throws ObjectAlreadyExists {
        PropertyTypeHandler existingPropertyTypeHandler = propertyTypeHandlerFacade.findByName(propertyTypeHandler.getName());
        if (existingPropertyTypeHandler != null) {
            throw new ObjectAlreadyExists("Property type handler " + propertyTypeHandler.getName() + " already exists.");
        }
        logger.debug("Inserting new property type " + propertyTypeHandler.getName());
    }

    @Override
    public void prepareEntityUpdate(PropertyTypeHandler propertyHandler) throws ObjectAlreadyExists {
    }   

    @Override
    protected PropertyTypeHandlerSettings createNewSettingObject() {
        return new PropertyTypeHandlerSettings(this);
    }

    /**
     * Converter class for property handler objects.
     */    
    @FacesConverter(value = "propertyTypeHandlerConverter", forClass = PropertyTypeHandler.class)
    public static class PropertyHandlerControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            try {
                PropertyTypeHandlerController controller = (PropertyTypeHandlerController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "propertyTypeHandlerController");
                return controller.getEntity(getIntegerKey(value));
            } catch (Exception ex) {
                // we cannot get entity from a given key
                logger.warn("Value " + value + " cannot be converted to property type handler object.");
                return null;
            }
        }

        Integer getIntegerKey(String value) {
            return Integer.valueOf(value);
        }

        String getStringKey(Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PropertyTypeHandler) {
                PropertyTypeHandler o = (PropertyTypeHandler) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PropertyTypeHandler.class.getName());
            }
        }

    }

}
