
package org.jaudiotagger.tag;

import org.jaudiotagger.tag.images.Artwork;

import java.util.Iterator;
import java.util.List;


public interface Tag {

    public void setField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException;


    public void addField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException;


    public void deleteField(FieldKey fieldKey) throws KeyNotFoundException;


    public void deleteField(String key)throws KeyNotFoundException;


    public List<TagField> getFields(String id);


    public List<TagField> getFields(FieldKey id) throws KeyNotFoundException;



    public Iterator<TagField> getFields();



    public String getFirst(String id);


    public String getFirst(FieldKey id) throws KeyNotFoundException;


    public List<String> getAll(FieldKey id) throws KeyNotFoundException;


    public String getValue(FieldKey id, int n);


    public TagField getFirstField(String id);


    public TagField getFirstField(FieldKey id);



    public boolean hasCommonFields();


    public boolean hasField(FieldKey fieldKey);


    public boolean hasField(String id);


    public boolean isEmpty();


    //TODO, do we need this
    public String toString();




    public int getFieldCount();



    public int getFieldCountIncludingSubValues();


    //TODO is this a special field?
    public boolean setEncoding(String enc) throws FieldDataInvalidException;



    public List<Artwork> getArtworkList();


    public Artwork getFirstArtwork();


    public void deleteArtworkField() throws KeyNotFoundException;



    public TagField createField(Artwork artwork) throws FieldDataInvalidException;


    public void setField(Artwork artwork) throws FieldDataInvalidException;


    public void addField(Artwork artwork) throws FieldDataInvalidException;


    public void setField(TagField field) throws FieldDataInvalidException;


    public void addField(TagField field) throws FieldDataInvalidException;


    public TagField createField(FieldKey genericKey, String value) throws KeyNotFoundException, FieldDataInvalidException;


    public abstract TagField createCompilationField(boolean value) throws KeyNotFoundException, FieldDataInvalidException;

}