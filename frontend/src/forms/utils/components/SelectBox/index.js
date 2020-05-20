import React from 'react';
import PropTypes from 'prop-types';
import InputLabel from "@material-ui/core/InputLabel";
import Select from "@material-ui/core/Select";
import FormControl from "@material-ui/core/FormControl";

import {SimpleObject} from "forms/utils/types";

import './style.css';

const SelectBox = ({items, value, onChange, labelText}) => {
    const componentId = labelText.toLowerCase().replace(' ', '_');
    return <FormControl component={componentId} className='select-box-custom' fullWidth>
        <InputLabel id={"select-label-" + componentId}>{labelText}</InputLabel>
        <Select
            native
            labelId={"select-label" + componentId}
            value={items.length !== 0 ? value || '' : ''}
            onChange={evt => onChange(evt.target.value)}
        >
            <option value=""/>
            {items.map(type => <option key={type.id} value={type.id}>{type.name}</option>)}
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