import { configureStore, createSlice} from '@reduxjs/toolkit';
import axios from 'axios';

// Reply Slice
let reply  = createSlice({
    name : 'reply',
    initialState : {replyList:[]},
    reducers:{
        setReply:(state,action)=>{
            state.replyList = action.payload
        }
    },

})
const dev_url = "http://socialgallery-env-1.eba-mbftgxd4.ap-northeast-2.elasticbeanstalk.com"


export const {setReply} = reply.actions;

// postAll Slice
let postAll  = createSlice({
    name : 'postAll',
    initialState : {postAllList:[]},
    reducers:{
        setPostAll:(state,action)=>{
            state.postAllList = action.payload
        }
    },
})

export const {setPostAll} = postAll.actions;


// Upload Post Title Slice
let postTitle  = createSlice({
  name : 'postTitle',
  initialState : {postTitleList:[]},
  reducers:{
      setPostTitle:(state,action)=>{
          state.postTitleList = action.payload
      }
      
  },
})
export const {setPostTitle} = postTitle.actions;

// Upload Post content Slice
let postContent  = createSlice({
  name : 'postContent',
  initialState : {postContentList:[]},
  reducers:{
      setPostContent:(state,action)=>{
          state.postContentList = action.payload
      }
      
  },
})

export const {setPostContent} = postContent.actions;




// Search filter Slice
let searchImg  = createSlice({
  name : 'searchImg',
  initialState : {searchList:[]},
  reducers:{
      setSearchImg:(state,action)=>{
          state.searchList = action.payload
      }
  },
})

export const {setSearchImg} = searchImg.actions;





//configureStore
export default configureStore({
    reducer: {
        reply : reply.reducer,
        postAll : postAll.reducer,
        postTitle : postTitle.reducer,
        postContent : postContent.reducer,
        searchImg : searchImg.reducer,
    },
  })

  