import React, {useEffect} from "react";
import PropTypes from 'prop-types';

import {connect} from "App/Context";
import {Event} from "forms/utils/types";
import SingleMap from "forms/utils/components/ObjectGeoLocation/SingleMap";
import Container from "forms/utils/components/Container/ContainerRow";

import Users from "./Users";
import MainInfo from "./MainInfo";
import {getUsers} from "./actions";

const ViewForm = ({event, getUsers}) => {
    useEffect(() => {
        getUsers(event.id);
    }, [getUsers, event]);

    return <Container>
        <MainInfo/>
        <SingleMap
            editable={false}
            route={event.route.locations}
        />
        <Users/>
    </Container>;
};

ViewForm.propTypes = {
    event: Event.isRequired,
    getUsers: PropTypes.func.isRequired,
};

const mapStateToProps = state => ({
    event: state.components.forms.event.eventView.event.response
});

export default connect(mapStateToProps, {getUsers})(ViewForm);