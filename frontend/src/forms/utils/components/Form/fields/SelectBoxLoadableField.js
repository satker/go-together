import React from 'react';
import PropTypes from "prop-types";

import MultipleSelectBox from "forms/utils/components/MultipleSelectBox";
import LoadableContent from "forms/utils/components/LoadableContent";
import {ResponseData, SimpleObject} from "forms/utils/types";
import ItemContainer from "forms/utils/components/Container/ItemContainer";

const SelectBoxLoadableField = ({name, options, placeholder, value, setValue, error}) => {
    return <ItemContainer style={{height: 70}}>
        <LoadableContent loadableData={options} name={name}>
            <MultipleSelectBox label={placeholder}
                               error={error}
                               value={value || []}
                               optionsSimple={options.response.result}
                               onChange={(languages) => setValue(name, languages)}/>
        </LoadableContent>
    </ItemContainer>;
};

SelectBoxLoadableField.propTypes = {
    name: PropTypes.string.isRequired,
    options: ResponseData,
    placeholder: PropTypes.string,
    value: PropTypes.arrayOf(SimpleObject),
    setValue: PropTypes.func,
    error: PropTypes.string
};

export default SelectBoxLoadableField;