import React, {useEffect} from "react";
import PropTypes from 'prop-types';
import './style.css';
import {connect} from "../../../../App/Context";
import {setFormId} from "./actions";
import {FORM_ID} from "./constants";

const ContainerColumn = ({children, isBordered, style, formId, setFormId}) => {
    useEffect(() => {
        if (formId) {
            setFormId(formId);
        }
    }, [formId, setFormId]);

    const styleClass = 'container-main-info margin-bottom-20' + (isBordered ? ' custom-border' : '');
    return <div className={styleClass} style={style}>{children}</div>
};

ContainerColumn.propTypes = {
    children: PropTypes.node,
    isBordered: PropTypes.bool,
    style: PropTypes.string,
    formId: PropTypes.string,
    setFormId: PropTypes.func.isRequired
};

export default connect(null, {setFormId})(ContainerColumn)(FORM_ID);