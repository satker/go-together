import React, {useContext} from "react";
import Profile from "./Profile";
import Reviews from "./Reviews";
import {Context} from "../../Context";
import {ApartmentView} from "../../types";
import FormReference from "../../utils/components/FormReference";
import ObjectGeoLocation from "../../utils/components/ObjectGeoLocation";
import Rating from "@material-ui/lab/Rating";
import Container from "@material-ui/core/Container";
import ListOfUsers from "./ListOfUsers";

const ViewEvent = ({event}) => {
    const [state] = useContext(Context);

    return <Container>
        {state.userId === event.author.id &&
        <FormReference formRef={'/events/' + event.id + '/edit'} description='Edit event'/>}

        <div className='container-main-info'>
            <div className='container-main-info-item fixed-width-content '>
                <div className='margin-right-item'>
                    <h4>{event.name}</h4>
                </div>
                <div className='margin-right-item'>
                    <Rating
                        size='small'
                        value={event.peopleLiked}
                        readOnly/>
                </div>
                <div className='margin-right-item' dangerouslySetInnerHTML={{__html: event.description}}/>
                <div className='margin-right-item'>
                    <h5>Event paid things:</h5><br/>
                    {event.paidThings.map((p, key) => {
                        return (<p key={key}>• {p.cashCategory} - {p.paidThing.name}</p>
                        )
                    })}
                </div>
            </div>
            <div className='container-main-info-item center-items fixed-width-profile'>
                <Profile user={event.author}/>
            </div>
        </div>
        Max count of users: {event.peopleCount}
        <ListOfUsers users={event.users}/>
        <b>Route: </b> {event.route.map(location => location.location.name + ", " +
        location.location.country.name).join(" -> ")}
        <ObjectGeoLocation
            header={event.name}
            latitude={event.route[0].latitude}
            longitude={event.route[0].longitude}
            draggable={false}
            zoom={15}
        />
        <Reviews eventId={event.id}/>
    </Container>;
};

ViewEvent.propTypes = {
    event: ApartmentView.isRequired
};

export default ViewEvent;