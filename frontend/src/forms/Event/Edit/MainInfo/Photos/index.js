import React from "react";
import GroupItems from "../../../../utils/components/CardItems";
import ImageSelector from "../../../../utils/components/ImageSelector";
import {Event} from "../../../../utils/types";
import PropTypes from "prop-types";

const Photos = ({event, onChangeEvent}) => {
    return <>
        <GroupItems items={event.eventPhotoDto.photos}
                    isPhotos
                    onDelete={(id) => console.log('delete: ', id)}/>
        <ImageSelector
            photos={event.eventPhotoDto.photos}
            setPhotos={(photos) => onChangeEvent('eventPhotoDto.photos', photos)}
            multiple={true}
        /></>
};

Photos.propTypes = {
    event: Event.isRequired,
    onChangeEvent: PropTypes.func.isRequired
};

export default Photos;