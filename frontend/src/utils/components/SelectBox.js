import React from 'react';
import InputLabel from "@material-ui/core/InputLabel";
import Select from "@material-ui/core/Select";
import FormControl from "@material-ui/core/FormControl";
import {SimpleObject} from "../../types";
import PropTypes from 'prop-types';

const SelectBox = ({items, value, onChange, labelText}) => {
    const componentId = labelText.toLowerCase().replace(' ', '_');
    return <FormControl component={componentId} className='select-box-custom'>
        <InputLabel id={"select-label-" + componentId}>{labelText}</InputLabel>
        <Select
            native
            labelId={"select-label" + componentId}
            value={items.length !== 0 ? value || '' : ''}
            onChange={onChange}
        >
            <option value=""/>
            {items.map(type => <option key={type.id} value={type.id}>{type.name}</option>)}
        </Select>
    </FormControl>
};

SelectBox.propTypes = {
    items: PropTypes.arrayOf(SimpleObject),
    value: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
    labelText: PropTypes.string.isRequired,
};

export default SelectBox;