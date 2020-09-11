import React from "react";
import Checkbox from "@material-ui/core/Checkbox";
import FormControlLabel from "@material-ui/core/FormControlLabel";

const CheckBox = ({value, setValue, label}) => {
    const handleChange = (event) => {
        setValue(event.target.checked);
    };

    return <FormControlLabel control={<Checkbox
        checked={value}
        onChange={handleChange}
    />} label={label}/>
};

export default CheckBox;