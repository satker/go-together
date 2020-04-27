import React from "react";
import * as PropTypes from "prop-types";
import {Event, ResponseData} from "../../../utils/types";
import CommonInfo from "./CommonInfo";
import Profile from "./Profile";
import ContainerColumn from "../../../utils/components/Container/ContainerColumn";

const MainInfo = ({event, users, setRefresh}) => {
    return <ContainerColumn>
        <CommonInfo setRefresh={setRefresh} users={users} event={event}/>
        <Profile user={event.author}/>
    </ContainerColumn>
};

MainInfo.propTypes = {
    event: Event.isRequired,
    users: ResponseData.isRequired,
    setRefresh: PropTypes.func.isRequired
};

export default MainInfo;