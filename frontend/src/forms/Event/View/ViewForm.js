import React, {useEffect} from "react";
import {connect} from "../../../App/Context";
import {Event} from "../../utils/types";
import FormReference from "../../utils/components/FormReference";
import ObjectGeoLocation from "../../utils/components/ObjectGeoLocation";
import Users from "./Users";
import MainInfo from "./MainInfo";
import Container from "../../utils/components/Container/ContainerRow";
import {getUsers} from "./actions";
import PropTypes from 'prop-types';

const ViewForm = ({event, getUsers, userId}) => {
    useEffect(() => {
        getUsers(event.id);
    }, [getUsers, event]);

    return <Container>
        {userId === event.author.id &&
        <FormReference formRef={'/events/' + event.id + '/edit'} description='Edit event'/>}

        <MainInfo/>
        <ObjectGeoLocation
            editable={false}
            setCurrentCenter={() => null}
            routes={event.route}
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

const mapStateToProps = () => state => ({
    event: state.components.forms.event.eventView.event.response,
    userId: state.userId.value
});

export default connect(mapStateToProps, {getUsers})(ViewForm);