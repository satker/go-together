import React from 'react';

import MultipleSelectBox from "forms/utils/components/MultipleSelectBox";
import LoadableContent from "forms/utils/components/LoadableContent";
import ItemContainer from "../../Container/ItemContainer";

const SelectBoxLoadableField = ({name, options, placeholder, value, setValue, error}) => {
    return <ItemContainer style={{height: 70}}>
        <LoadableContent loadableData={options} name={name}>
            <MultipleSelectBox label={placeholder}
                               error={error}
                               value={value || []}
                               optionsSimple={options.response}
                               onChange={(languages) => setValue(name, languages)}/>
        </LoadableContent>
    </ItemContainer>;
};

export default SelectBoxLoadableField;