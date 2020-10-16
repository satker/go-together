import React from 'react';

import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {NotificationMessage} from "forms/utils/types";
import {getCorrectDateFromString} from "forms/utils/utils";

const Notification = ({notification}) => {
    return <ItemContainer>
        {getCorrectDateFromString(notification.date) + ": " + notification.message}
    </ItemContainer>;
}

Notification.propTypes = {
    notification: NotificationMessage.isRequired
}

export default Notification;