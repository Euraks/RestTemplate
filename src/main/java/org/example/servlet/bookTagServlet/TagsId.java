package org.example.servlet.bookTagServlet;

//
//@WebServlet(name = "TagsId", value = "/tags/*")
//public class TagsId extends HttpServlet {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger( Simples.class );
//
//    ObjectMapper mapper = new ObjectMapper();
//    private final ConnectionManager connectionManager;
//    private final Repository<TagEntity, UUID> repository;
//    private Service<TagEntity, UUID> service;
//
//    public TagsId(ConnectionManager connectionManager) {
//        this.connectionManager = connectionManager;
//        this.repository = new TagRepositoryImpl( this.connectionManager );
//        this.service = new TagServiceImpl( repository );
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String pathInfo = request.getPathInfo();
//
//        if (pathInfo != null && !pathInfo.isEmpty()) {
//            String[] pathParts = pathInfo.split("/");
//            if (pathParts.length > 1) {
//                UUID tagUUID = UUID.fromString(pathParts[pathParts.length - 1]);
//                try {
//                    Optional<TagEntity> optionalTag = service.findById(tagUUID);
//                    if (optionalTag.isPresent()) {
//                        TagOutGoingDTO tagOutGoingDTO = TagMapper.INSTANCE.map(optionalTag.get());
//                        String jsonString = mapper.writeValueAsString(tagOutGoingDTO);
//
//                        response.setContentType("application/json");
//                        response.setCharacterEncoding("UTF-8");
//                        response.getWriter().write("Get TagEntity UUID:" + jsonString);
//                    } else {
//                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                        response.getWriter().write("TagEntity not found");
//                    }
//                } catch (IllegalArgumentException e) {
//                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                    response.getWriter().write("Invalid UUID format");
//                }
//            } else {
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                response.getWriter().write("ID is required");
//            }
//        } else {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("ID is required");
//        }
//    }
//
//    @Override
//    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String pathInfo = request.getPathInfo();
//
//        setResponseDefaults(response);
//
//        if (pathInfo == null || pathInfo.isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("ID is required");
//            return;
//        }
//
//        String[] pathParts = pathInfo.split("/");
//        if (pathParts.length <= 1) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("ID is required");
//            return;
//        }
//
//        StringBuilder sb = getStringFromRequest(request);
//        UUID tagId = UUID.fromString(pathParts[1]);
//        String json = sb.toString();
//
//        TagUpdateDTO tagUpdateDTO = mapper.readValue(json, TagUpdateDTO.class);
//        TagEntity updateTagEntity = TagMapper.INSTANCE.map(tagUpdateDTO);
//        Optional<TagEntity> optNewTagEntity = service.findById(tagId);
//
//        if (!optNewTagEntity.isPresent()) {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            response.getWriter().write("TagEntity not found");
//            return;
//        }
//
//        TagEntity newTagEntity = optNewTagEntity.get();
//        newTagEntity.setTagName(updateTagEntity.getTagName());
//        newTagEntity.setBookEntities(updateTagEntity.getBookEntities());
//
//        try {
//            service.save(newTagEntity);
//            response.getWriter().write("Updated TagEntity UUID:" + json);
//        } catch (SQLException e) {
//            LOGGER.error("Error while updating TagEntity with UUID: " + tagId, e);
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("Failed to update TagEntity due to a database error");
//        }
//    }
//
//
//    private StringBuilder getStringFromRequest(HttpServletRequest request) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        String line;
//        try (BufferedReader reader = request.getReader()){
//            while ((line = reader.readLine()) != null) {
//                sb.append( line );
//            }
//        }
//        return sb;
//    }
//
//    @Override
//    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String pathInfo = request.getPathInfo();
//
//        setResponseDefaults(response);
//
//        if (pathInfo != null && !pathInfo.isEmpty()) {
//            String[] pathParts = pathInfo.split("/");
//            if (pathParts.length > 1) {
//                UUID tagUUID = UUID.fromString(pathParts[pathParts.length - 1]);
//                if (service.delete(tagUUID)) {
//                    response.getWriter().write("Delete TagEntity UUID:" + tagUUID);
//                    LOGGER.l( Level.INFO, "Successfully deleted TagEntity with UUID: {0}", tagUUID);
//                } else {
//                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                    response.getWriter().write("TagEntity with UUID:" + tagUUID + " not found");
//                    LOGGER.log(Level.WARNING, "TagEntity with UUID: {0} not found", tagUUID);
//                }
//                return;
//            }
//        }
//
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        response.getWriter().write("ID is required for deletion");
//        LOGGER.log(Level.WARNING, "ID is required for deletion");
//    }
//
//    private void setResponseDefaults(HttpServletResponse response) {
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//    }
//}
//
//
