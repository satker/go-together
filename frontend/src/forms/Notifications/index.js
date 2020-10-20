import React, {useEffect, useState} from 'react';
import PropTypes from 'prop-types';

import {connect} from "App/Context";
import Container from "forms/utils/components/Container/ContainerRow";
import {NotificationObject} from "forms/utils/types";

import Notification from "./Notification";
import {getUserNotifications, readNotifications} from "./actions";
import LoadableContent from "../utils/components/LoadableContent";
import CustomPagination from "../utils/components/Pagination";

const Notifications = ({userNotifications, readNotifications, userId, getUserNotifications}) => {
    const [page, setPage] = useState(1);

    useEffect(() => {
        if (userId) {
            getUserNotifications(userId, page - 1);
        }
    }, [getUserNotifications, userId, page]);

    useEffect(() => {
        if (userId) {
            readNotifications(userId);
        }
    }, [readNotifications, userId]);

    const pageCount = userNotifications.response.page ?
        Math.ceil(userNotifications.response.page.totalSize / userNotifications.response.page.size) : 0;
    console.log(pageCount)
    return <Container>
        <LoadableContent loadableData={userNotifications}>
            {(userNotifications.response?.result || []).map(notification =>
                <Notification notification={notification.notificationMessage} key={notification.id}/>
            )}
        </LoadableContent>
        {pageCount <= 1 || <CustomPagination pageCount={pageCount}
                                             page={page}
                                             setPage={setPage}/>}
    </Container>
};

Notifications.propTypes = {
    userNotifications: PropTypes.arrayOf(NotificationObject).isRequired,
    readNotifications: PropTypes.func.isRequired,
    userId: PropTypes.string
};

const mapStateToProps = state => ({
    userId: state.auth.value.userId,
    userNotifications: state.components.forms.notifications.userNotifications
});

export default connect(mapStateToProps, {readNotifications, getUserNotifications})(Notifications);