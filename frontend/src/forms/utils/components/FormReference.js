import React from 'react';
import * as PropTypes from "prop-types";
import {A} from "hookrouter";

const FormReference = ({formRef, action, description}) =>
    <A href={formRef} onClick={action ? action : () => null}>{description}</A>;

FormReference.propTypes = {
    formRef: PropTypes.string.isRequired,
    action: PropTypes.func,
    description: PropTypes.string
};

export default FormReference;