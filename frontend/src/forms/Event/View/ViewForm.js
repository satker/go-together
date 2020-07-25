import React, {useEffect} from "react";
import PropTypes from 'prop-types';

import {connect} from "App/Context";
import {Event} from "forms/utils/types";
import Reference from "forms/utils/components/Reference";
import ObjectGeoLocation from "forms/utils/components/ObjectGeoLocation";
import Container from "forms/utils/components/Container/ContainerRow";

import Users from "./Users";
import MainInfo from "./MainInfo";
import {getUsers} from "./actions";

const ViewForm = ({event, getUsers, userId}) => {
    useEffect(() => {
        getUsers(event.id);
    }, [getUsers, event]);

    return <Container>
        {userId === event.author.id &&
        <Reference formRef={'/events/' + event.id + '/edit'} description='Edit event'/>}

        <MainInfo/>
        <ObjectGeoLocation
            editable={false}
            setCurrentCenter={() => null}
            routes={event.route.locations}
            height={400}
        />
        <Users/>
    </Container>;
};

ViewForm.propTypes = {
    event: Event.isRequired,
    getUsers: PropTypes.func.isRequired,
    userId: PropTypes.string
};

const mapStateToProps = state => ({
    event: state.components.forms.event.eventView.event.response,
    userId: state.userId.value
});

export default connect(mapStateToProps, {getUsers})(ViewForm);