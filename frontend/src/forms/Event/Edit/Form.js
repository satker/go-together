import React, {useEffect} from 'react';
import {navigate} from 'hookrouter';
import PropTypes from "prop-types";

import {connect} from "App/Context";
import {Event, ResponseData} from "forms/utils/types";
import CustomButton from "forms/utils/components/CustomButton";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import Container from "forms/utils/components/Container/ContainerRow";

import MainInfo from "./MainInfo";
import Route from "./Route";
import {updateEvent} from "./actions";

const EditForm = ({
                      event, userId, save,
                      updatedEvent, newEvent, clean
                  }) => {

    const saveEvent = () => {
        let saveObj = {...event};
        saveObj.id = event.id;
        saveObj.author = {
            id: userId
        };
        const maxRouteNumber = saveObj.routeInfo.infoRoutes.length;
        saveObj.routeInfo.infoRoutes = saveObj.routeInfo.infoRoutes.map(route => {
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
        save(saveObj);
    };

    useEffect(() => {
        const id = updatedEvent.response.id || newEvent.response.id;
        if (id) {
            navigate('/events/' + id);
            clean();
        }
    }, [updatedEvent, newEvent]);

    return <Container>
        <MainInfo/>
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
    save: PropTypes.func.isRequired,
    updatedEvent: ResponseData.isRequired,
    newEvent: ResponseData.isRequired,
    clean: PropTypes.func.isRequired,
};

const mapStateToProps = (state) => ({
    userId: state.auth.response.userId,
    updatedEvent: state.components.forms.event.eventEdit.updatedEvent,
    newEvent: state.components.forms.event.eventEdit.newEvent,
    event: state.components.forms.event.eventEdit.event.response
});

export default connect(mapStateToProps, {updateEvent})(EditForm);