package com.github.chameleon.intellij.alm;

import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.metadata.EntityMetadata;
import com.hpe.adm.nga.sdk.metadata.Features.Feature;
import com.hpe.adm.nga.sdk.metadata.FieldMetadata;
import com.hpe.adm.nga.sdk.model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Guy Guetta on 19/06/2016.
 * Creates data for use with the SDK
 */
public class DataGenerator {

    public static EntityModel generateEntityWithRequiredFields(NGA nga, String entityName) throws Exception {
        Collection<FieldMetadata> requiredFields = nga.metadata().fields(entityName).execute();
        requiredFields.removeIf(field -> !field.isRequired());

        Set<FieldModel> fields = new HashSet<>();
        requiredFields.stream().forEach(field -> fields.add(generateField(nga,entityName, field)));

        return new EntityModel(fields);
    }

    private static FieldModel generateField(NGA nga, String entityName, FieldMetadata fieldModel) {
        String fieldName = fieldModel.getName();
        FieldModel field;
        switch (fieldModel.getFieldType()) {
            case String:
                field = new StringFieldModel(fieldName, "Guy");
                break;
            case Boolean:
                field = new BooleanFieldModel(fieldName, false);
                break;
            case Long:
                field = new LongFieldModel(fieldName, 55l);
                break;
//            case Reference:
//                field = generateRef(nga, entityName, fieldName);
//                break;
            default:
                field = null;
        }

        return field;
    }

    private static ReferenceFieldModel generateRef(NGA nga, String entityName, String fieldName) {
        Collection<EntityMetadata> entityMetadatas = nga.metadata().entities("defect").execute();
        EntityMetadata entityMetadata = entityMetadatas.iterator().next();
        Collection<Feature> result = entityMetadata.features().stream()
                .filter(feature -> feature.getName().equals("subtype_of")).collect(Collectors.toList());

        Feature hierarchyFeature = result.iterator().next();


        return new ReferenceFieldModel("hi",new EntityModel("hi","hello"));
    }

}
