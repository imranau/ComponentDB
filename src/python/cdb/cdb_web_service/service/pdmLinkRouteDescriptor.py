#!/usr/bin/env python

#
# PdmLink route descriptor.
#

from cdb.common.utility.configurationManager import ConfigurationManager
from pdmLinkController import PdmLinkController


class PDMLinkRouteDescriptor:

    @classmethod
    def getRoutes(cls):
        contextRoot = ConfigurationManager.getInstance().getContextRoot()

        # Static instances shared between different routes
        pdmLinkController = PdmLinkController()

        # Define routes.
        routes = [

            # Get drawing 
            {
                'name': 'getDrawing',
                'path': '%s/pdmLink/drawings/:(name)' % contextRoot,
                'controller': pdmLinkController,
                'action': 'getDrawing',
                'method': ['GET']
            },

            # Get multiple drawings using keywords/wildcards
            {
                'name': 'getDrawings',
                'path': '%s/pdmLink/drawingsByKeyword/:(drawingNamePattern)' % contextRoot,
                'controller': pdmLinkController,
                'action': 'getDrawings',
                'method': ['GET']
            },

            # Search for drawings using using keywords/wildcards
            {
                'name': 'getDrawings',
                'path': '%s/pdmLink/search/:(drawingNamePattern)' % contextRoot,
                'controller': pdmLinkController,
                'action': 'getDrawingSearchResults',
                'method': ['GET']
            },

            # Get PdmLink drawing thumbnail 
            {
                'name': 'getDrawingThumbnail',
                'path': '%s/pdmLink/drawingThumbnails/:(ufid)' % contextRoot,
                'controller': pdmLinkController,
                'action': 'getDrawingThumbnail',
                'method': ['GET']
            },

            # Get PdmLink drawing image 
            {
                'name': 'getDrawingImage',
                'path': '%s/pdmLink/drawingImages/:(ufid)' % contextRoot,
                'controller': pdmLinkController,
                'action': 'getDrawingImage',
                'method': ['GET']
            }

        ]
       
        return routes


