
package org.jaudiotagger.tag.datatype;

import org.jaudiotagger.tag.id3.framebody.FrameBodySYTC;


public class SynchronisedTempoCodeList extends AbstractDataTypeList<SynchronisedTempoCode>
{


    public SynchronisedTempoCodeList(final SynchronisedTempoCodeList copy)
    {
        super(copy);
    }

    public SynchronisedTempoCodeList(final FrameBodySYTC body)
    {
        super(DataTypes.OBJ_SYNCHRONISED_TEMPO_LIST, body);
    }

    @Override
    protected SynchronisedTempoCode createListElement()
    {
        return new SynchronisedTempoCode(DataTypes.OBJ_SYNCHRONISED_TEMPO, frameBody);
    }
}
