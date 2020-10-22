import React from "react";
import {connect} from "../../Context";
import {navigate} from "hookrouter";
import Badge from "@material-ui/core/Badge";
import EditIcon from '@material-ui/icons/Edit';
import IconButton from "@material-ui/core/IconButton";

import {FORM_ID as FORM_ID_EVENT_VIEW} from "forms/Event/View/constants";

const EditButton = ({eventAuthorId, userId, menuId, eventId, formName}) => {
    if (formName !== FORM_ID_EVENT_VIEW || eventAuthorId !== userId) {
        return null;
    }
    return <IconButton
        onClick={() => navigate('/events/' + eventId + '/edit', true)}
        key={menuId + '_create'}
        color="inherit">
        <Badge color="secondary">
            <EditIcon/>
        </Badge>
    </IconButton>;
};

const mapStateToProps = (state) => ({
    formName: state.formId.value,
    userId: state.auth.response.userId,
    eventAuthorId: state.components.forms.event.eventView.event.response.author?.id,
    eventId: state.components.forms.event.eventView.event.response.id
});

export default connect(mapStateToProps)(EditButton);