import React from 'react';

import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {NotificationMessageType} from "forms/utils/types";
import {getCorrectDateFromString} from "forms/utils/utils";

const NotificationRow = ({notification}) => {
    return <ItemContainer>
        {getCorrectDateFromString(notification.date) + ": " + notification.message}
    </ItemContainer>;
}

NotificationRow.propTypes = {
    notification: NotificationMessageType.isRequired
}

export default NotificationRow;