import React from "react";
import * as PropTypes from "prop-types";
import Button from "@material-ui/core/Button";

const CustomButton = ({text, disabled, onClick, color}) => {
    return <Button color={color}
                   disabled={disabled}
                   onClick={onClick}>{text}</Button>
};

CustomButton.propTypes = {
    text: PropTypes.string.isRequired,
    disabled: PropTypes.bool,
    onClick: PropTypes.func.isRequired,
    color: PropTypes.string
};

CustomButton.defaultProps = {
    color: 'default'
};

export default CustomButton;