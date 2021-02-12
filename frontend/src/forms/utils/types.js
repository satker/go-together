import PropTypes from "prop-types";

export const NamedType = PropTypes.exact({
    id: PropTypes.string,
    name: PropTypes.string
});

export const Country = PropTypes.exact({
    id: PropTypes.string,
    countryCode: PropTypes.string,
    name: PropTypes.string
})

export const Place = PropTypes.exact({
    id: PropTypes.string,
    name: PropTypes.string,
    state: PropTypes.string,
    country: Country
});

export const SimpleObject = PropTypes.exact({
    id: PropTypes.string,
    name: PropTypes.string,
    code: PropTypes.string
});

export const NotificationMessageType = PropTypes.exact({
    id: PropTypes.string,
    message: PropTypes.string,
    date: PropTypes.string,
    notification: PropTypes.exact({
        id: PropTypes.string,
        producerId: PropTypes.string
    })
})

export const NotificationType = PropTypes.exact({
    id: PropTypes.string,
    isRead: PropTypes.string,
    notificationMessage: NotificationMessageType.isRequired
});

export const ResponseData = PropTypes.exact({
    type: PropTypes.string.isRequired,
    headers: PropTypes.string,
    inProcess: PropTypes.bool.isRequired,
    response: PropTypes.oneOfType([PropTypes.array, PropTypes.object]),
    error: PropTypes.oneOfType([PropTypes.string, PropTypes.bool])
});

export const PhotoType = PropTypes.exact({
    id: PropTypes.string,
    photoUrl: PropTypes.string,
    content: PropTypes.exact({
        type: PropTypes.string,
        photoContent: PropTypes.string
    })
});

export const Language = PropTypes.exact({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    code: PropTypes.string.isRequired
});

export const Interest = PropTypes.exact({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
});

export const GroupPhoto = PropTypes.exact({
    id: PropTypes.string,
    groupId: PropTypes.string,
    category: PropTypes.string,
    photos: PropTypes.arrayOf(PhotoType)
});

export const Location = PropTypes.exact({
    id: PropTypes.string,
    address: PropTypes.string,
    latitude: PropTypes.number,
    longitude: PropTypes.number,
    place: Place
});

export const User = PropTypes.exact({
    id: PropTypes.string,
    login: PropTypes.string,
    mail: PropTypes.string,
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    location: Location,
    description: PropTypes.string,
    password: PropTypes.string,
    role: PropTypes.string,
    groupPhoto: GroupPhoto,
    languages: PropTypes.arrayOf(Language),
    interests: PropTypes.arrayOf(Interest)
});

export const SimpleUser = PropTypes.exact({
    id: PropTypes.string,
    login: PropTypes.string,
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    userPhoto: PhotoType,
});

export const EventUser = PropTypes.exact({
    id: PropTypes.string,
    user: SimpleUser,
    userStatus: PropTypes.string,
    eventId: PropTypes.string
});

export const RouteInfoItem = PropTypes.exact({
    id: PropTypes.string,
    location: Location,
    transportType: PropTypes.string,
    cost: PropTypes.number,
    movementDate: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
    movementDuration: PropTypes.number,
    routeNumber: PropTypes.number,
    isStart: PropTypes.bool,
    isEnd: PropTypes.bool
});

export const RouteInfo = PropTypes.exact({
    id: PropTypes.string,
    groupId: PropTypes.string,
    infoRoutes: PropTypes.arrayOf(RouteInfoItem)
});

export const Event = PropTypes.exact({
    id: PropTypes.string,
    name: PropTypes.string,
    author: User,
    peopleCount: PropTypes.number,
    description: PropTypes.string,
    groupPhoto: GroupPhoto,
    routeInfo: RouteInfo,
    startDate: PropTypes.oneOfType([PropTypes.string, PropTypes.object]),
    endDate: PropTypes.oneOfType([PropTypes.string, PropTypes.object])
});

export const Review = PropTypes.exact({
    id: PropTypes.string,
    rating: PropTypes.number,
    date: PropTypes.object,
    message: PropTypes.string,
    authorId: PropTypes.string,
    messageType: PropTypes.string,
    recipientId: PropTypes.string
});

export const SearchObject = PropTypes.exact({
    page: PropTypes.exact({
        page: PropTypes.number.isRequired,
        size: PropTypes.number.isRequired,
        totalSize: PropTypes.number.isRequired,
        sort: PropTypes.array.isRequired
    }),
    mainIdField: PropTypes.string,
    filters: PropTypes.object
});

export const MapRoute = PropTypes.exact({
    routeNumber: PropTypes.number,
    location: Location
})

export const EventMapRoute = PropTypes.exact({
    id: PropTypes.string,
    name: PropTypes.string,
    locations: PropTypes.arrayOf(MapRoute)
})

