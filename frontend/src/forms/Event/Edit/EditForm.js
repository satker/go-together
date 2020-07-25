import React, {useEffect} from 'react';
import {navigate} from 'hookrouter';
import PropTypes from "prop-types";

import {connect} from "App/Context";
import {Event, ResponseData} from "forms/utils/types";
import CustomButton from "forms/utils/components/CustomButton";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import Container from "forms/utils/components/Container/ContainerRow";

import PaidThings from "./PaidThings";
import MainInfo from "./MainInfo";
import Route from "./Route";
import {postUpdatedEvent, putNewEvent, updateEvent} from "./actions";

const EditForm = ({
                      event, userId, postUpdatedEvent, putNewEvent,
                      updatedEvent, newEvent
                  }) => {

    const saveEvent = () => {
        let saveObj = {...event};
        saveObj.id = event.id;
        saveObj.author = {
            id: userId
        };
        const maxRouteNumber = saveObj.route.locations.length;
        saveObj.route.locations = saveObj.route.locations.map(route => {
            if (route.routeNumber === 1) {
                route.isStart = true;
                route.isEnd = false;
            } else if (route.routeNumber === maxRouteNumber) {
                route.isStart = false;
                route.isEnd = true;
            } else {
                route.isStart = false;
                route.isEnd = false;
            }
            return route;
        });
        event.id ? postUpdatedEvent(saveObj) : putNewEvent(saveObj);
    };

    useEffect(() => {
        const id = updatedEvent.response.id || newEvent.response.id;
        if (id) {
            navigate('/events/' + id)
        }
    }, [updatedEvent, newEvent]);

    return <Container>
        <MainInfo/>
        <PaidThings/>
        <Route/>
        <ItemContainer>
            <CustomButton color="primary"
                          text='Save'
                          onClick={saveEvent}/>
        </ItemContainer>
    </Container>
};

EditForm.propTypes = {
    event: Event.isRequired,
    userId: PropTypes.string,
    postUpdatedEvent: PropTypes.func.isRequired,
    putNewEvent: PropTypes.func.isRequired,
    updatedEvent: ResponseData.isRequired,
    newEvent: ResponseData.isRequired
};

const mapStateToProps = (state) => ({
    userId: state.userId.value,
    updatedEvent: state.components.forms.event.eventEdit.updatedEvent,
    newEvent: state.components.forms.event.eventEdit.newEvent,
    event: state.components.forms.event.eventEdit.event.response
});

export default connect(mapStateToProps, {postUpdatedEvent, putNewEvent, updateEvent})(EditForm);