package com.github.chameleon.intellij.alm;

import com.hpe.adm.nga.sdk.NGA;
import com.hpe.adm.nga.sdk.Query;
import com.hpe.adm.nga.sdk.model.*;
import com.hpe.adm.nga.sdk.utils.CommonUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Guy Guetta on 19/06/2016.
 * Creates dummy data.
 * Each method is pretty self-explanatory
 */
public class DummyGenerator {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static Collection<EntityModel> generateEntityModel(NGA nga, String entityName, Set<FieldModel> fields) throws Exception {
        Collection<EntityModel> entities = new ArrayList<>();
        switch (entityName) {
            case "releases":
                entities.add(generateRelease());
                break;
            case "milestones":
                entities.add(generateMilestone());
                break;
            case "features":
                entities.add(generateFeature(nga, fields));
                break;
            case "defects":
                entities.add(generateDefect(nga, fields));
                break;
            case "product_areas":
                entities.add(generatePA(nga, fields));
                break;
        }
        return entities;
    }

    public static Collection<EntityModel> generateEntityModel(NGA nga, String entityName) throws Exception {
        Set<FieldModel> fields = new HashSet<>();
        return generateEntityModel(nga, entityName, fields);
    }

    public static Collection<EntityModel> generateEntityModelCollection(NGA nga, String entityName) throws Exception {
        Collection<EntityModel> entities = new ArrayList<>();

        entities.addAll(generateEntityModel(nga, entityName));
        entities.addAll(generateEntityModel(nga, entityName));
        entities.addAll(generateEntityModel(nga, entityName));

        return entities;
    }

    public static List<String> generateNamesForUpdate() {
        List<String> generatedValues = new ArrayList<>();
        generatedValues.add("updatedName" + UUID.randomUUID());
        generatedValues.add("updatedName" + UUID.randomUUID());
        generatedValues.add("updatedName" + UUID.randomUUID());
        return generatedValues;
    }


    private static EntityModel generatePA(NGA nga, Set<FieldModel> fields) throws Exception {
        Collection<EntityModel> pas = nga.entityList("product_areas").get().execute();
        EntityModel parentEntity = CommonUtils.getEntityWithStringValue(pas, "parent", null);
        long parentId = CommonUtils.getIdFromEntityModel(parentEntity);
        Set<FieldModel> parentFields = new HashSet<>();
        parentFields.add(new LongFieldModel("id", parentId));
        parentFields.add(new StringFieldModel("type", "product_area"));
        EntityModel parent = new EntityModel(parentFields);

        FieldModel name = new StringFieldModel("name", "sdk_pa_" + UUID.randomUUID());
        FieldModel parentField = new ReferenceFieldModel("parent", parent);

        fields.add(name);
        fields.add(parentField);
        return new EntityModel(fields);
    }

    private static EntityModel generateFeature(NGA nga, Set<FieldModel> fields) throws Exception {
        Collection<EntityModel> phases = nga.entityList("phases").get().execute();
        EntityModel phase = phases.iterator().next();
        Collection<EntityModel> themes = nga.entityList("themes").get().execute();
        EntityModel theme = themes.iterator().next();

        FieldModel name = new StringFieldModel("name", "sdk_feature_" + UUID.randomUUID());
        FieldModel phaseField = new ReferenceFieldModel("phase", phase);
        FieldModel parentField = new ReferenceFieldModel("parent", theme);

        fields.add(name);
        fields.add(phaseField);
        fields.add(parentField);
        return new EntityModel(fields);
    }

    private static EntityModel generateDefect(NGA nga, Set<FieldModel> fields) throws Exception {
        Query query = new Query().field("subtype").equal("work_item_root").build();
        Collection<EntityModel> roots = nga.entityList("work_items").get().query(query).execute();
        EntityModel root = roots.iterator().next();
        FieldModel parentField = new ReferenceFieldModel("parent", root);

        Collection<EntityModel> users = nga.entityList("workspace_users").get().execute();
        EntityModel user = users.iterator().next();
        FieldModel author = new ReferenceFieldModel("author", user);

        Query query2 = new Query().field("entity").equal("defect").build();
        Collection<EntityModel> phases = nga.entityList("phases").get().query(query2).execute();
        EntityModel phase = phases.iterator().next();
        FieldModel phaseField = new ReferenceFieldModel("phase", phase);

        FieldModel name = new StringFieldModel("name", "sdk_defect_" + UUID.randomUUID());

        Collection<EntityModel> listNodes = nga.entityList("list_nodes").get().execute();
        EntityModel severity = CommonUtils.getEntityWithStringValue(listNodes, "logical_name", "list_node.severity.low");
        FieldModel severityField = new ReferenceFieldModel("severity", severity);

        fields.add(name);
        fields.add(author);
        fields.add(phaseField);
        fields.add(parentField);
        fields.add(severityField);
        return new EntityModel(fields);
    }

    private static EntityModel generateRelease() throws ParseException {
        Set<FieldModel> fields = new HashSet<>();
        FieldModel name = new StringFieldModel("name", "sdk_release_" + UUID.randomUUID());
        FieldModel startDate = new DateFieldModel("start_date", DATE_FORMAT.parse("2015-03-14T12:00:00Z"));
        FieldModel endDate = new DateFieldModel("end_date", DATE_FORMAT.parse("2016-03-14T12:00:00Z"));
        fields.add(name);
        fields.add(startDate);
        fields.add(endDate);
        return new EntityModel(fields);
    }

    private static EntityModel generateMilestone() throws ParseException {
        Set<FieldModel> fields = new HashSet<>();
        FieldModel name = new StringFieldModel("name", "sdk_milestone_" + UUID.randomUUID());
        FieldModel date = new DateFieldModel("date", DATE_FORMAT.parse("2016-03-17T12:00:00Z"));
        fields.add(name);
        fields.add(date);
        return new EntityModel(fields);
    }
}
