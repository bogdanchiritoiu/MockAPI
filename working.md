# Working

## Json structure
- project name
  - endpoint name
  - methods: all, get, post, put, delete (configurable)
  - type: usually _String_ 'need to investigate more'

## Workflow
1. Start app
2. Parse the JSON file
   1. Structure of JSON file
   2. Customizability
3. Create and populate the database based on the endpoints structure
4. Create the endpoints using _RequestMappingHandlerMapping_ and _RequestMappingInfo_

### Create and populate the database

### Create the endpoints