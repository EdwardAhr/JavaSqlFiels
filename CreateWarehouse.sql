INSERT INTO warehouse
( id
, code
, "name"
, active
, updated_at
, updated_by
, created_at
, created_by
, legal_entity_id
, yard_id
, application_module
, size_unit_of_measure_id
, volume_unit_of_measure_id
, weight_unit_of_measure_id)
VALUES ( gen_random_uuid()
       , '${warehouseCode:-AutotestCode}'
       , '${warehouseName:-AutotestName}'
       , '${active:-true}'
       , null
       , '${updatedBy:-Autotest}'
       , localtimestamp
       , '${createdBy:-Autotest}'
       , '${legalEntityCode:-AutotestCode}'
       , null
       , 'WAREHOUSE'
       , '${sizeUnitOfMeasureCode:-м}'
       , '${volumeUnitOfMeasureCode:-м3}'
       , '${weightUnitOfMeasureCode:-кг}');