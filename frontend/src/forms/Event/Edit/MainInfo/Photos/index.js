import React from "react";
import GroupItems from "../../../../utils/components/CardItems";
import ImageSelector from "../../../../utils/components/ImageSelector";
import {Event} from "../../../../utils/types";
import PropTypes from "prop-types";
import ItemContainer from "../../../../utils/components/Container/ItemContainer";

const Photos = ({event, onChangeEvent}) => {
    return <>
        <ItemContainer>
            <GroupItems items={event.eventPhotoDto.photos}
                        isPhotos
                        onDelete={(id) => console.log('delete: ', id)}/>
        </ItemContainer>
        <ItemContainer>
            <ImageSelector
                photos={event.eventPhotoDto.photos}
                setPhotos={(photos) => onChangeEvent('eventPhotoDto.photos', photos)}
                multiple={true}
            /></ItemContainer></>
};

Photos.propTypes = {
    event: Event.isRequired,
    onChangeEvent: PropTypes.func.isRequired
};

export default Photos;