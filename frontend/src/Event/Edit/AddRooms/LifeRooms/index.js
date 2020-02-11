import React, {useContext, useEffect, useState} from 'react';
import Button from "reactstrap/es/Button";
import GroupItems from "../../../../utils/components/CardItems";
import {CONTENT_SERVICE_URL, DEFAULT_ROOM} from "../../../../utils/constants";
import {Context} from "../../../../Context";
import PropTypes from "prop-types";
import {getRandomNum} from "../../../../utils/utils";
import {Room} from "../../../../types";

const LifeRooms = ({lifeRooms, onChange}) => {
    const [lifeType, setLifeType] = useState(null);
    const [lifeRoom, setLifeRoom] = useState(null);

    const [state] = useContext(Context);

    const onDelete = (id) => onChange(lifeRooms.filter(room => room.id !== id));

    useEffect(() => {
        if (!lifeType) {
            state.fetchWithToken(CONTENT_SERVICE_URL + '/rooms/types/life', lifeType => {
                setLifeType(lifeType);
                const lifeRoom = {...DEFAULT_ROOM};
                lifeRoom.roomType = lifeType.id;
                setLifeRoom(lifeRoom);
            });
        }
    }, [state, lifeType, setLifeType]);

    const newLifeRoom = {...lifeRoom};

    const onChangeRoom = room => onChange([...lifeRooms].map(item => item.id === room.id ? room : item));

    return <>
        <br/>
        {lifeRoom &&
        <Button outline color="success" onClick={() => onChange([...lifeRooms,
            (newLifeRoom.id = getRandomNum(), newLifeRoom)])}>Add room</Button>}
        <br/>
        <br/>
        <GroupItems items={lifeRooms}
                    onChange={onChangeRoom}
                    isRooms
                    isLifeRoom={true}
                    editable
                    onDelete={onDelete}/>
    </>;
};

LifeRooms.propTypes = {
    lifeRooms: PropTypes.arrayOf(Room).isRequired,
    onChange: PropTypes.func.isRequired
};

export default LifeRooms;