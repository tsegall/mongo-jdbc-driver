package com.dbschema.mongo.schema;


import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.dbschema.mongo.nashorn.JMongoUtil.escapeChars;

public class MetaField {

  public final MetaJson parentJson;
  public final String name, typeName;
  public final List<ObjectId> objectIds = new ArrayList<ObjectId>();
  public final int type;
  public final List<MetaReference> references = new ArrayList<MetaReference>();
  public boolean mandatory = true;


  public MetaField(final MetaJson parentJson, final String name, final String typeName, int type) {
    this.parentJson = parentJson;
    this.name = name;
    this.typeName = typeName;
    this.type = type;
  }

  public void addObjectId(ObjectId objectId) {
    if (objectIds.size() < 4) {
      objectIds.add(objectId);
    }
  }

  public String getNameWithPath() {
    String qualifier = parentJson != null && !(parentJson instanceof MetaCollection) ? parentJson.getNameWithPath() + "." : "";
    return qualifier + escapeChars(name, '\\', '.');
  }

  public String getPkColumnName() {
    String pkColumnName = name;
    final int idx = pkColumnName.lastIndexOf('.');
    if (idx > 0) {
      pkColumnName = pkColumnName.substring(idx + 1);
    }
    return pkColumnName;
  }

  public MetaReference createReferenceTo(MetaCollection pkCollection) {
    MetaReference ifk = new MetaReference(this, pkCollection);
    references.add(ifk);
    return ifk;
  }

  public MetaCollection getMetaCollection() {
    MetaField field = this;
    while (field != null && !(field instanceof MetaCollection)) {
      field = field.parentJson;
    }
    return (MetaCollection) field;
  }

  public void collectFieldsWithObjectId(List<MetaField> unsolvedFields) {
    if (!objectIds.isEmpty()) {
      unsolvedFields.add(this);
    }
  }

  @Override
  public String toString() {
    return getNameWithPath();
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public boolean isMandatory() {
    return mandatory;
  }
}
