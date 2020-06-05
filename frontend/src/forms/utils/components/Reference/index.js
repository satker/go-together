import React from 'react';
import PropTypes from "prop-types";
import {A} from "hookrouter";

const Reference = ({formRef, action, description}) =>
    <A href={formRef} onClick={action ? action : () => null}>{description}</A>;

Reference.propTypes = {
    formRef: PropTypes.string.isRequired,
    action: PropTypes.func,
    description: PropTypes.string
};

export default Reference;