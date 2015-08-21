package no.nb.microservices.delivery.service;

/**
 * Created by andreasb on 16.07.15.
 */
//@RunWith(MockitoJUnitRunner.class)
//public class OrderServiceTest {
//
//    @InjectMocks
//    OrderService orderService;
//
//    @Mock
//    TextualService textualService;
//
//    @Mock
//    ZipService zipService;
//
//    @Mock
//    DeliveryMetadataService deliveryMetadataService;
//
//    @Mock
//    EmailService emailService;
//
//    @Mock
//    ApplicationSettings applicationSettings;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void placeOrderTest() throws ExecutionException, InterruptedException, IOException {
//        ItemOrder itemOrder = getItemOrderOne();
//
//        Resource resource = new ClassPathResource("ecd270f69cb8a9063306fcecd4b1a769.pdf");
//        InputStream inputStream = resource.getInputStream();
//        ByteArrayResource byteArrayResource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
//        TextualResource textualResource = new TextualResource(itemOrder.getTextualRequests().get(0).getUrn(), TextualFormat.PDF, byteArrayResource);
//        Future<List<TextualResource>> textualResourceListFuture = CompletableFuture.completedFuture(Arrays.asList(textualResource));
//        when(textualService.getResourcesAsync(eq(itemOrder.getTextualRequests().get(0)))).thenReturn(textualResourceListFuture);
//
//        when(applicationSettings.getZipFilePath()).thenReturn("");
//
//        Resource zippedfile = new ClassPathResource("ecd270f69cb8a9063306fcecd4b1a769.zip");
//        when(zipService.zipIt(anyString(), any(List.class))).thenReturn(zippedfile.getFile());
//
//        when(deliveryMetadataService.saveOrder(any(OrderMetadata.class))).then(returnsFirstArg());
//
//        orderService.placeOrder(itemOrder);
//
//        ArgumentCaptor<Email> argumentCaptor = ArgumentCaptor.forClass(Email.class);
//        verify(emailService, times(1)).sendEmail(argumentCaptor.capture());
//        OrderMetadata itemOrderCaptor = (OrderMetadata)argumentCaptor.getValue().getContent();
//        assertEquals(itemOrder.getEmailTo(), argumentCaptor.getValue().getTo());
//        assertTrue(itemOrderCaptor.getExpireDate().after(Date.from(Instant.now())));
//        assertTrue(itemOrderCaptor.getKey().matches("^\\w{16}$"));
//    }
//
//    private ItemOrder getItemOrderOne() {
//        TextualRequest textualRequest = new TextualRequest() {{
//            setUrn("URN:NBN:no-nb_digibok_2014020626009");
//            setFormat(TextualFormat.PDF);
//            setPages("ALL");
//            setQuality(6);
//            setText(false);
//        }};
//
//        ItemOrder itemOrder = new ItemOrder() {{
//            setEmailTo("example@example.com");
//            setEmailCc("example-cc@example.com");
//            setPurpose("Testing purpose");
//            setCompressionType(CompressionType.ZIP);
//            setTextualRequests(Arrays.asList(textualRequest));
//            setAudioRequests(null);
//            setVideoRequests(null);
//            setPhotoRequests(null);
//        }};
//
//        return itemOrder;
//    }
//}
