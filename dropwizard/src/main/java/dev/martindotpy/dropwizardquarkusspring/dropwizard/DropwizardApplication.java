package dev.martindotpy.dropwizardquarkusspring.dropwizard;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import dev.martindotpy.dropwizardquarkusspring.dropwizard.core.adapter.controller.MiscellaneousController;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.core.adapter.controller.OpenApiController;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.adapter.controller.NoteController;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.adapter.repository.NoteMongoRepository;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.usecase.CreateNoteUseCase;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.usecase.DeleteNoteUseCase;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.usecase.FindNoteUseCase;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.application.usecase.UpdateNoteUseCase;
import dev.martindotpy.dropwizardquarkusspring.dropwizard.note.domain.model.Note;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.lifecycle.Managed;

public class DropwizardApplication extends Application<DropwizardConfiguration> {
    public static void main(final String[] args) throws Exception {
        new DropwizardApplication().run(args);
    }

    @Override
    public String getName() {
        return "dropwizard";
    }

    @Override
    public void initialize(final Bootstrap<DropwizardConfiguration> bootstrap) {
    }

    @Override
    public void run(final DropwizardConfiguration configuration, final Environment environment) {
        // MongoDB setup
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings mongoSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(configuration.getMongo().getUri()))
                .codecRegistry(pojoCodecRegistry)
                .build();

        MongoClient mongoClient = MongoClients.create(mongoSettings);

        environment.lifecycle().manage(new Managed() {
            @Override
            public void start() {
            }

            @Override
            public void stop() {
                mongoClient.close();
            }
        });

        MongoDatabase database = mongoClient.getDatabase(configuration.getMongo().getDatabase());

        // Repositories and Use cases
        NoteMongoRepository noteRepository = new NoteMongoRepository(database.getCollection("note", Note.class));

        FindNoteUseCase findNoteUseCase = new FindNoteUseCase(noteRepository);
        CreateNoteUseCase createNoteUseCase = new CreateNoteUseCase(noteRepository);
        UpdateNoteUseCase updateNoteUseCase = new UpdateNoteUseCase(noteRepository);
        DeleteNoteUseCase deleteNoteUseCase = new DeleteNoteUseCase(noteRepository);

        environment.jersey().register(new MiscellaneousController());
        environment.jersey().register(new OpenApiController());
        environment.jersey().register(
                new NoteController(findNoteUseCase, createNoteUseCase, updateNoteUseCase, deleteNoteUseCase));
    }
}
