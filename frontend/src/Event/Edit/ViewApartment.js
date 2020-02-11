import React, {useContext, useState} from 'react';
import MainInfo from "./MainInfo";
import AddRooms from "./AddRooms";
import Button from "reactstrap/es/Button";
import {Context} from "../../Context";
import {EVENT_SERVICE_URL} from "../../utils/constants";
import {CreateApartment} from "../../types";
import ObjectGeoLocation from "../../utils/components/ObjectGeoLocation";
import {get, set} from 'lodash';
import {navigate} from 'hookrouter';
import Container from "@material-ui/core/Container";

const ViewApartment = ({apartment}) => {
    const [createApartment, setCreateApartment] = useState(apartment);

    const [state] = useContext(Context);

    const saveApartment = () => {
        let saveObj = {...createApartment};
        saveObj.id = createApartment.id;
        saveObj.userId = state.userId;
        if (createApartment.id) {
            state.fetchWithToken(EVENT_SERVICE_URL + '/apartments/update', (response) =>
                response.id && navigate('/apartments/' + response.id), 'POST', saveObj);
        } else {
            state.fetchWithToken(EVENT_SERVICE_URL + '/apartments', (response) =>
                response && navigate('/apartments/' + response.id), 'POST', saveObj);
        }
    };

    const onChangeField = (obj, field) => {
        const updatedApartment = {...createApartment};
        const currentValue = get(updatedApartment, field);
        if (currentValue !== obj) {
            set(updatedApartment, field, obj);
            setCreateApartment(updatedApartment);
        }
    };

    const onChangeRooms = (isLifeRoom) => (rooms) => {
        const updatedApartment = {...createApartment};
        setCreateApartment((updatedApartment.rooms[isLifeRoom ? 'LIFE_ROOMS' : 'OTHER_ROOMS'] = rooms, updatedApartment));
    };

    return <Container>
        <MainInfo apartment={createApartment}
                  onChange={onChangeField}/>
        <ObjectGeoLocation
            onChange={onChangeField}
            draggable={true}
            longitude={createApartment.location.longitude ? createApartment.location.longitude : 73.8567}
            latitude={createApartment.location.latitude ? createApartment.location.latitude : 18.5204}
            header={'V'}
            height={400}
        />
        <AddRooms lifeRooms={createApartment.rooms.LIFE_ROOMS}
                  otherRooms={createApartment.rooms.OTHER_ROOMS}
                  onChange={onChangeRooms}/>

        <Button className="btn btn-success" onClick={saveApartment}>Save</Button>
    </Container>
};

ViewApartment.propTypes = {
    apartment: CreateApartment.isRequired
};

export default ViewApartment;