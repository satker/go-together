import React from 'react';
import CardMedia from "@material-ui/core/CardMedia";

import {getSrcForImg} from "forms/utils/utils";
import ErrorMessage from "forms/utils/components/LoadableContent/ErrorMessage";
import ImageSelector from "forms/utils/components/ImageSelector";

const PhotoField = ({name, value, setValue, error}) => {
    return <>
        {value && <CardMedia style={{height: '255px', width: '350px'}}
                             component="img"
                             image={getSrcForImg(value[0])}/>}
        <ImageSelector photos={value}
                       setPhotos={photo => {
                           setValue(name, [photo])
                       }}
                       multiple={false}
        />
        {error && <ErrorMessage error={error}/>}
    </>;
}

export default PhotoField;