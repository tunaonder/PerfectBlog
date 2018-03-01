/* 
 * Created by Sait Tuna Onder on 2018.02.28  * 
 * Copyright © 2018 Sait Tuna Onder. All rights reserved. * 
 */

var postId;

function postClicked(id) {
    this.postId = id;    
    localStorage.setItem( 'objectToPass', id );
    location.href = 'ReadPost.xhtml';
}