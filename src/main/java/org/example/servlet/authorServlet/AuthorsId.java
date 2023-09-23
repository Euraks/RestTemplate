package org.example.servlet.authorServlet;

//
//@WebServlet(name = "AuthorsId", value = "/authors/*")
//public class AuthorsId extends HttpServlet {
//
//    private static final Logger LOGGER = Logger.getLogger( AuthorsId.class.getName());
//
//    ObjectMapper mapper = new ObjectMapper();
//    private final AuthorEntityRepository<AuthorEntity, UUID> repository = new AuthorEntityRepositoryImpl();
//    private final AuthorEntityService service = new AuthorEntityServiceImpl( repository );
//
//
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String pathInfo = request.getPathInfo();
//        if (pathInfo != null && !pathInfo.isEmpty()) {
//            String[] pathParts = pathInfo.split( "/" );
//            if (pathParts.length > 1) {
//                String id = pathParts[1];
//                AuthorEntity authorEntity = service.findById( UUID.fromString( id ) );
//                 AuthorEntityOutGoingDTO authorEntityOutGoingDTO = AuthorEntityMapper.INSTANCE.map( authorEntity);
//                String jsonString = mapper.writeValueAsString( authorEntityOutGoingDTO );
//                response.setContentType( "application/json" );
//                response.setCharacterEncoding( "UTF-8" );
//                response.getWriter().write("Get SimpleEntity UUID:"+ jsonString );
//                return;
//            }
//        }
//    }
//
//    @Override
//    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String pathInfo = request.getPathInfo();
//        if (pathInfo != null && !pathInfo.isEmpty()) {
//            String[] pathParts = pathInfo.split( "/" );
//            if (pathParts.length > 1) {
//                StringBuilder sb = getStringFromRequest( request );
//                UUID authorId = UUID.fromString( pathParts[1]);
//                String json = sb.toString();
//
//                ObjectMapper objectMapper = new ObjectMapper();
//                AuthorEntityUpdateDTO authorEntityUpdateDTO = objectMapper.readValue( json, AuthorEntityUpdateDTO.class );
//
//                AuthorEntity updateAuthorEntity = AuthorEntityMapper.INSTANCE.map( authorEntityUpdateDTO );
//                AuthorEntity newAuthorEntity = service.findById( authorId );
//                newAuthorEntity.setAuthorName( updateAuthorEntity.getAuthorName() );
//                newAuthorEntity.setArticleList( updateAuthorEntity.getArticleList() );
//                try{
//                    service.save( newAuthorEntity);
//                } catch(SQLException e){
//                    e.printStackTrace();
//                }
//                response.setContentType( "application/json" );
//                response.setCharacterEncoding( "UTF-8" );
//                response.getWriter().write("Updated SimpleEntity UUID:"+ json );
//                return;
//            }
//        }
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
//                String id = pathParts[1];
//                try {
//                    if (service.delete(UUID.fromString(id))) {
//                        response.getWriter().write("Delete SimpleEntity UUID:" + id);
//                        LOGGER.log(Level.INFO, "Successfully deleted SimpleEntity with UUID: {0}", id);
//                    } else {
//                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                        response.getWriter().write("SimpleEntity with UUID:" + id + " not found");
//                        LOGGER.log(Level.WARNING, "SimpleEntity with UUID: {0} not found", id);
//                    }
//                    return;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                    response.getWriter().write("Failed to delete SimpleEntity UUID:" + id);
//                    LOGGER.log( Level.SEVERE, "Failed to delete SimpleEntity with UUID: {0}", id);
//                    return;
//                }
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
//}
