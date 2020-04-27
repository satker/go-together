import React from 'react';
import Select from "react-select";

const MultipleSelectBox = ({optionsSimple, value, onChange, label}) =>
    <>
        {label}:
        <Select options={optionsSimple.map(param => ({value: param.id, label: param.name}))}
                isMulti
                value={value}
                onChange={onChange}
        />
    </>;

export default MultipleSelectBox;