import React from "react";
import TextField from "@material-ui/core/TextField";
import PropTypes from 'prop-types';
import {withStyles} from "@material-ui/core";

const StyledTextField = withStyles({
    MuiInputLabelOutlined: {
        transform: 'translate(14px, -10px) scale(0.75)'
    },
    MuiInputLabelShrink: {
        transform: 'translate(14px, -10px) scale(0.75)'
    }
})(TextField);

const LabeledInput = ({value, onChange, isError, errorText, id, label, defaultValue, multiline, rowsMax, type}) => {
    const onChangeValue = (evt) => {
        onChange(evt.target.value)
    };

    return <TextField style={{background: 'white'}}
                      type={type}
                      multiline={multiline}
                      error={isError}
                      id={id}
                      rowsMax={rowsMax}
                      label={label}
                      helperText={errorText}
                      variant="outlined"
                      value={value}
                      defaultValue={defaultValue}
        onChange={onChangeValue}
    />
};

LabeledInput.propTypes = {
    value: PropTypes.string,
    onChange: PropTypes.func.isRequired,
    isError: PropTypes.bool,
    errorText: PropTypes.string,
    id: PropTypes.string.isRequired,
    label: PropTypes.string,
    defaultValue: PropTypes.string,
    multiline: PropTypes.bool,
    rowsMax: PropTypes.number,
    type: PropTypes.string
};

LabeledInput.defaultProps = {
    type: 'text'
};

export default LabeledInput;