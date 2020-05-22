import React from 'react';
import PropTypes from 'prop-types';
import Select from "@material-ui/core/Select";
import MenuItem from "@material-ui/core/MenuItem";

import {SimpleObject} from "forms/utils/types";
import Input from "@material-ui/core/Input";
import Chip from "@material-ui/core/Chip";
import {makeStyles} from "@material-ui/core/styles";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";

const useStyles = makeStyles(theme => ({
    formControl: {
        margin: theme.spacing.unit
    },
    chips: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    chip: {
        margin: theme.spacing.unit / 4,
    }
}));

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
    PaperProps: {
        style: {
            maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
            width: 250,
        },
    },
};

const MultipleSelectBox = ({optionsSimple, value, onChange, label}) => {
    const handleChange = (event) => onChange(event.target.value);
    const classes = useStyles();

    return <FormControl className={classes.formControl} fullWidth>
        <InputLabel htmlFor="select-multiple-chip">{label}</InputLabel>
        <Select
            multiple
            value={value}
            onChange={handleChange}
            input={<Input id="select-multiple-chip"/>}
            renderValue={selected => (
                <div className={classes.chips}>
                    {selected.map(option => (
                        <Chip key={option.id} label={option.name} className={classes.chip}/>
                    ))}
                </div>
            )}
            MenuProps={MenuProps}
        >
            {optionsSimple.map(option => (
                <MenuItem key={option.id} value={option}>
                    {option.name}
                </MenuItem>
            ))}
        </Select>
    </FormControl>;
};

MultipleSelectBox.propTypes = {
    optionsSimple: PropTypes.arrayOf(SimpleObject),
    value: PropTypes.arrayOf(SimpleObject),
    onChange: PropTypes.func.isRequired,
    label: PropTypes.string
};

export default MultipleSelectBox;