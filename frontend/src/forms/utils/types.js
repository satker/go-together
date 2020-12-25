import PropTypes from "prop-types";

export const Place = PropTypes.exact({
    id: PropTypes.string,
    name: PropTypes.string,
    state: PropTypes.string,
    country: Country
});

export const Country = PropTypes.exact({
    id: PropTypes.string,
    countryCode: PropTypes.string,
    name: PropTypes.string
})

export const GroupLocation = PropTypes.exact({
    id: PropTypes.string,
    groupId: PropTypes.string,
    locations: PropTypes.arrayOf(Location),
    category: PropTypes.string
});

export const SimpleObject = PropTypes.exact({
    id: PropTypes.string,
    name: PropTypes.string,
    code: PropTypes.string
});

export const NotificationMessage = PropTypes.exact({
    id: PropTypes.string,
    message: PropTypes.string,
    date: PropTypes.string,
    isRead: PropTypes.bool,
    notificationId: PropTypes.string
})

export const NotificationObject = PropTypes.exact({
    id: PropTypes.string,
    isRead: PropTypes.bool,
    notificationMessage: NotificationMessage.isRequired
});

export const ResponseData = PropTypes.exact({
    type: PropTypes.string.isRequired,
    headers: PropTypes.string,
    inProcess: PropTypes.bool.isRequired,
    response: PropTypes.oneOfType([PropTypes.array, PropTypes.object]),
    error: PropTypes.oneOfType([PropTypes.string, PropTypes.bool])
});

export const PhotoObject = PropTypes.exact({
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

export const User = PropTypes.exact({
    id: PropTypes.string,
    login: PropTypes.string,
    mail: PropTypes.string,
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    location: GroupLocation,
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
    userPhoto: PhotoObject,
});

export const EventUser = PropTypes.exact({
    id: PropTypes.string,
    user: SimpleUser,
    userStatus: PropTypes.string,
    eventId: PropTypes.string
});

export const GroupPhoto = PropTypes.exact({
    id: PropTypes.string,
    groupId: PropTypes.string,
    category: PropTypes.string,
    photos: PropTypes.arrayOf(PhotoObject)
});

export const Location = PropTypes.exact({
    id: PropTypes.string,
    routeNumber: PropTypes.number.isRequired,
    address: PropTypes.string,
    latitude: PropTypes.number,
    longitude: PropTypes.number,
    place: Place,
    isStart: PropTypes.bool,
    isEnd: PropTypes.bool,
});

export const Event = PropTypes.exact({
    id: PropTypes.string,
    name: PropTypes.string.isRequired,
    author: User,
    peopleCount: PropTypes.number.isRequired,
    description: PropTypes.string.isRequired,
    groupPhoto: GroupPhoto,
    route: GroupLocation,
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

export const CoordinateCenter = PropTypes.exact({
    lat: PropTypes.number.isRequired,
    lng: PropTypes.number.isRequired,
});
