import React from 'react';

import MultipleSelectBox from "forms/utils/components/MultipleSelectBox";
import LoadableContent from "forms/utils/components/LoadableContent";

const SelectBoxLoadableField = ({name, options, placeholder, value, setValue, error}) => {
    return <LoadableContent loadableData={options} name={name}>
        <MultipleSelectBox label={placeholder}
                           error={error}
                           value={value || []}
                           optionsSimple={options.response}
                           onChange={(languages) => setValue(name, languages)}/>
    </LoadableContent>;
};

export default SelectBoxLoadableField;