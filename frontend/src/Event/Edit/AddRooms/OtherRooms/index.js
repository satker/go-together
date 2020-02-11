import React from 'react';
import Button from "reactstrap/es/Button";
import GroupItems from "../../../../utils/components/CardItems";
import PropTypes from "prop-types";
import {DEFAULT_ROOM} from "../../../../utils/constants";
import {getRandomNum} from "../../../../utils/utils";
import {Room} from "../../../../types";

const OtherRooms = ({otherRooms, onChange}) => {
    const defaultRoom = {...DEFAULT_ROOM};

    const onDelete = (id) => onChange(otherRooms.filter(room => room.id !== id));

    const onChangeRoom = room => onChange([...otherRooms].map(item => item.id === room.id ? room : item));

    return <>
        <br/>
        <Button outline color="success"
                onClick={() => onChange([...otherRooms, (defaultRoom.id = getRandomNum(), defaultRoom)])}>
            Add room
        </Button>
        <br/>
        <br/>
        <GroupItems items={otherRooms}
                    onChange={onChangeRoom}
                    isRooms isLifeRoom={false}
                    editable onDelete={onDelete}/>
    </>
};

OtherRooms.propTypes = {
    otherRooms: PropTypes.arrayOf(Room).isRequired,
    onChange: PropTypes.func.isRequired
};

export default OtherRooms;