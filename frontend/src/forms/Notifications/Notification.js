import React from 'react';
import moment from "moment";

import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {NotificationObject} from "forms/utils/types";

const Notification = ({notification}) => {
    return <ItemContainer>
        {moment(notification.date).format('LLL') + ": " + notification.message}
    </ItemContainer>;
}

Notification.propTypes = {
    notification: NotificationObject.isRequired
}

export default Notification;