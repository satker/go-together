import React from 'react';
import PropTypes from "prop-types";

import {connect} from "App/Context";

import {cleanNewEvent, cleanUpdatedEvent, postUpdatedEvent, putNewEvent} from "./actions";
import {showNotification} from "forms/utils/components/Notification/actions";
import Form from "forms/Event/Edit/Form";

const EditForm = ({
                      isUpdate, postUpdatedEvent, putNewEvent,
                      cleanUpdatedEvent, cleanNewEvent, showNotification
                  }) => {
    const save = (event) => isUpdate ? postUpdatedEvent(event) : putNewEvent(event);

    const clean = () => {
        isUpdate ? cleanUpdatedEvent() : cleanNewEvent();
        const notificationMessage = isUpdate ? 'Updated event successful' : 'Created event';
        showNotification(notificationMessage);
    }

    return <Form save={save} clean={clean}/>
};

EditForm.propTypes = {
    postUpdatedEvent: PropTypes.func.isRequired,
    putNewEvent: PropTypes.func.isRequired,
    cleanUpdatedEvent: PropTypes.func.isRequired,
    cleanNewEvent: PropTypes.func.isRequired,
    isUpdate: PropTypes.bool,
    showNotification: PropTypes.func.isRequired,
    cleanEvent: PropTypes.func.isRequired
};

export default connect(null, {
    postUpdatedEvent,
    putNewEvent,
    cleanUpdatedEvent,
    cleanNewEvent,
    showNotification
})(EditForm);