import React from 'react';
import PropTypes from "prop-types";

import './link_style.css'

const CustomReference = ({action, description}) => <button
    type="button"
    className="link-button"
    onClick={action}>
    {description}
</button>;

CustomReference.propTypes = {
    action: PropTypes.func.isRequired,
    description: PropTypes.string.isRequired

};

export default CustomReference;