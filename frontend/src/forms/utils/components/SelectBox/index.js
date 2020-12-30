import React from 'react';
import PropTypes from 'prop-types';
import InputLabel from "@material-ui/core/InputLabel";
import Select from "@material-ui/core/Select";
import FormControl from "@material-ui/core/FormControl";

import {SimpleObject} from "forms/utils/types";

import './style.css';
import {MenuItem} from "@material-ui/core";

const SelectBox = ({items, value, onChange, labelText}) => {
    const componentId = labelText.toLowerCase().replace(' ', '');
    return <FormControl component={componentId} className='select-box-custom' fullWidth>
        <InputLabel id={"select-label-" + componentId}>{labelText}</InputLabel>
        <Select
            labelId={"select-label" + componentId}
            value={items.length !== 0 ? value || '' : ''}
            onChange={evt => onChange(evt.target.value)}>
            {items.map(type => <MenuItem key={type.id} value={type.id}>{type.name}</MenuItem>)}
        </Select>
    </FormControl>
};

SelectBox.propTypes = {
    items: PropTypes.arrayOf(SimpleObject).isRequired,
    value: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
    labelText: PropTypes.string.isRequired,
};

export default SelectBox;