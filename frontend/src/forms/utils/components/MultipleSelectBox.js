import React from 'react';
import {FormGroup, Label} from "reactstrap";
import Select from "react-select";

const MultipleSelectBox = ({optionsSimple, value, onChange, label}) =>
    <FormGroup>
        <Label>{label}</Label>
        <Select options={optionsSimple.map(param => ({value: param.id, label: param.name}))}
                isMulti
                value={value}
                onChange={onChange}
        />
    </FormGroup>;

export default MultipleSelectBox;