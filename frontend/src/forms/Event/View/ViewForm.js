import React, {useEffect, useState} from "react";
import {connect} from "../../../App/Context";
import {Event, ResponseData} from "../../utils/types";
import FormReference from "../../utils/components/FormReference";
import ObjectGeoLocation from "../../utils/components/ObjectGeoLocation";
import Users from "./Users";
import MainInfo from "./MainInfo";
import Container from "../../utils/components/Container/ContainerRow";
import {FORM_ID} from "./constants";
import {getStatuses, getUsers} from "./actions";
import PropTypes from 'prop-types';

const ViewForm = ({event, statuses, users, getUsers, getStatuses, userId}) => {
    const [refresh, setRefresh] = useState(true);

    useEffect(() => {
        if (refresh) {
            getStatuses(event.id);
            getUsers(event.id);
            setRefresh(!refresh);
        }
    }, [getStatuses, getUsers, event, refresh, setRefresh]);

    return <Container formId={FORM_ID}>
        {userId === event.author.id &&
        <FormReference formRef={'/events/' + event.id + '/edit'} description='Edit event'/>}

        <MainInfo event={event}
                  setRefresh={setRefresh}
                  users={users}/>
        <ObjectGeoLocation
            editable={false}
            setCurrentCenter={() => null}
            routes={event.route}
            height={400}
        />
        <Users eventUserId={event.author.id}
               eventId={event.id}
               users={users}
               statuses={statuses}/>
    </Container>;
};

ViewForm.propTypes = {
    event: Event.isRequired,
    statuses: ResponseData.isRequired,
    users: ResponseData.isRequired,
    getUsers: PropTypes.func.isRequired,
    getStatuses: PropTypes.func.isRequired,
    userId: PropTypes.string
};

const mapStateToProps = (FORM_ID) => state => ({
    statuses: state[FORM_ID]?.statuses || [],
    users: state[FORM_ID]?.users || [],
    userId: state.userId
});

export default connect(mapStateToProps, {getUsers, getStatuses})(ViewForm)(FORM_ID);