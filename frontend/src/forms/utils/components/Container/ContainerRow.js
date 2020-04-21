import React, {useEffect} from "react";
import './style.css';
import PropTypes from "prop-types";
import {setFormId} from "./actions";
import {connect} from "../../../../App/Context";
import {FORM_ID} from './constants'

const Container = ({children, formId, setFormId}) => {
    useEffect(() => {
        if (formId) {
            setFormId(formId);
        }
    }, [formId, setFormId]);

    return <div className='container-main-info-common margin-bottom-20'>{children}</div>
};

Container.propTypes = {
    children: PropTypes.node,
    formId: PropTypes.string,
    setFormId: PropTypes.func.isRequired
};

export default connect(null, {setFormId})(Container)(FORM_ID);