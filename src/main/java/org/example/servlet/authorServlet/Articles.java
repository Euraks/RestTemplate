package org.example.servlet.authorServlet;

//
//@WebServlet(name = "Articles", value = "/articles")
//public class Articles extends HttpServlet {
//
//    ObjectMapper mapper = new ObjectMapper();
//    private final AuthorEntityRepository<AuthorEntity, UUID> repository = new AuthorEntityRepositoryImpl();
//    private final AuthorEntityService service = new AuthorEntityServiceImpl( repository );
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<Article> articleList = service.findArticlesAll();
//        ArticleAllOutGoingDTO articleAllOutGoingDTO = ArticleMapper.INSTANCE.mapListToDto( articleList );
//        String jsonString = mapper.writeValueAsString( articleAllOutGoingDTO );
//        response.setContentType( "application/json" );
//        response.setCharacterEncoding( "UTF-8" );
//        response.getWriter().write( "GetAll SimpleEntity UUID:" + jsonString );
//    }
//}
