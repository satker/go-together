import React from 'react';
import PropTypes from 'prop-types';
import Select from "@material-ui/core/Select";
import ListItemText from "@material-ui/core/ListItemText";
import Checkbox from "@material-ui/core/Checkbox";
import MenuItem from "@material-ui/core/MenuItem";
import {find} from "lodash";
import {SimpleObject} from "../types";

const MultipleSelectBox = ({optionsSimple, value, onChange, label}) => {
    const handleChange = (event) => onChange(event.target.value);

    return <Select multiple
                   label={label}
                   value={value}
                   renderValue={(selected) => {
                       if (selected.length === 0) {
                           return <em>Choose {label}</em>;
                       }

                       return selected.map(val => val.name).join(', ');
                   }}
                   fullWidth
                   onChange={handleChange}
    >
        {optionsSimple.map((param) => (
            <MenuItem key={param.id} value={param}>
                <Checkbox checked={!!find(value, ['id', param.id])}/>
                <ListItemText primary={param.name}/>
            </MenuItem>
        ))}
    </Select>;
};

MultipleSelectBox.propTypes = {
    optionsSimple: PropTypes.arrayOf(SimpleObject),
    value: PropTypes.arrayOf(SimpleObject),
    onChange: PropTypes.func.isRequired,
    label: PropTypes.string
};

export default MultipleSelectBox;